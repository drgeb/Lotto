#!/usr/bin/env python3
"""Statistical analysis of Lotto Aruba Mega Plus draws (4 numbers 1-30 + Mega Ball 1-15)."""
import re
from collections import Counter, defaultdict
from itertools import combinations
from math import comb, sqrt, exp

MD = "mega_plus_all_results.md"
TXT = "results.txt"

MAIN_MAX, MB_MAX, PICK = 30, 15, 4


def parse_md(path):
    rows = []
    pat = re.compile(r"^\|\s*(.+?)\s*\|\s*(\d+)\s*\|\s*([\d-]+)\s*\|\s*(\d+)\s*\|")
    with open(path) as f:
        for line in f:
            m = pat.match(line.strip())
            if m:
                date, draw, nums, mb = m.groups()
                n = [int(x) for x in nums.split("-")]
                if len(n) == PICK:
                    rows.append({"date": date, "draw": int(draw),
                                 "nums": sorted(n), "mb": int(mb)})
    return rows


def parse_txt(path):
    rows = []
    with open(path) as f:
        for line in f:
            p = line.strip().split("\t")
            if len(p) == 4:
                n = [int(x) for x in p[2].split("-")]
                if len(n) == PICK:
                    rows.append({"date": p[0], "draw": int(p[1]),
                                 "nums": sorted(n), "mb": int(p[3])})
    return rows


md_rows = parse_md(MD)
txt_rows = parse_txt(TXT)

# Merge, de-duplicate by draw number (prefer md, which is zero-padded/consistent)
by_draw = {}
for r in txt_rows + md_rows:
    by_draw[r["draw"]] = r
draws = sorted(by_draw.values(), key=lambda r: r["draw"])

print("=" * 72)
print("DATASET")
print("=" * 72)
print(f"mega_plus_all_results.md : {len(md_rows)} draws (draw #{md_rows[-1]['draw']} - #{md_rows[0]['draw']})")
print(f"results.txt              : {len(txt_rows)} draws (draw #{txt_rows[-1]['draw']} - #{txt_rows[0]['draw']})")
print(f"merged, deduped          : {len(draws)} draws (draw #{draws[0]['draw']} - #{draws[-1]['draw']})")
print(f"date range               : {draws[0]['date']} -> {draws[-1]['date']}")
print(f"game                     : pick {PICK} of {MAIN_MAX}, Mega Ball of {MB_MAX}")

N = len(draws)
main_c = Counter()
mb_c = Counter()
pair_c = Counter()
for d in draws:
    main_c.update(d["nums"])
    mb_c[d["mb"]] += 1
    pair_c.update(combinations(d["nums"], 2))

exp_main = N * PICK / MAIN_MAX
exp_mb = N / MB_MAX


def chi2(counter, k, expected):
    return sum((counter.get(i, 0) - expected) ** 2 / expected for i in range(1, k + 1))


# p-value approximation for chi-square (Wilson-Hilferty)
def chi2_p(x, df):
    if df <= 0:
        return 1.0
    z = ((x / df) ** (1 / 3) - (1 - 2 / (9 * df))) / sqrt(2 / (9 * df))
    return 0.5 * (1 - _erf(z / sqrt(2)))


def _erf(x):
    # Abramowitz-Stegun 7.1.26
    t = 1 / (1 + 0.3275911 * abs(x))
    y = 1 - (((((1.061405429 * t - 1.453152027) * t) + 1.421413741) * t
              - 0.284496736) * t + 0.254829592) * t * exp(-x * x)
    return y if x >= 0 else -y


print()
print("=" * 72)
print("1. MAIN NUMBER FREQUENCY (1-30) - full history")
print("=" * 72)
print(f"{'Num':>4} {'Hits':>5} {'Exp':>6} {'%':>6} {'+/-':>6}  bar")
ranked = sorted(range(1, MAIN_MAX + 1), key=lambda i: -main_c[i])
for i in ranked:
    h = main_c[i]
    bar = "#" * round(h / exp_main * 20)
    print(f"{i:>4} {h:>5} {exp_main:>6.1f} {100*h/N:>6.2f} {h-exp_main:>+6.1f}  {bar}")

x2 = chi2(main_c, MAIN_MAX, exp_main)
print(f"\nChi-square = {x2:.2f} on df={MAIN_MAX-1}  ->  p = {chi2_p(x2, MAIN_MAX-1):.3f}")
print("(p < 0.05 would suggest non-randomness; p > 0.05 = consistent with fair draws)")

print()
print("=" * 72)
print("2. MEGA BALL FREQUENCY (1-15) - full history")
print("=" * 72)
print(f"{'MB':>4} {'Hits':>5} {'Exp':>6} {'%':>6} {'+/-':>6}  bar")
for i in sorted(range(1, MB_MAX + 1), key=lambda i: -mb_c[i]):
    h = mb_c[i]
    bar = "#" * round(h / exp_mb * 20)
    print(f"{i:>4} {h:>5} {exp_mb:>6.1f} {100*h/N:>6.2f} {h-exp_mb:>+6.1f}  {bar}")
x2m = chi2(mb_c, MB_MAX, exp_mb)
print(f"\nChi-square = {x2m:.2f} on df={MB_MAX-1}  ->  p = {chi2_p(x2m, MB_MAX-1):.3f}")

# --- recency-weighted hot numbers ---
print()
print("=" * 72)
print("3. RECENCY-WEIGHTED 'HOT' NUMBERS (last 200 draws, exponential decay)")
print("=" * 72)
last = draws[-200:]
wmain, wmb = defaultdict(float), defaultdict(float)
for k, d in enumerate(last):
    w = 0.5 ** ((len(last) - 1 - k) / 60.0)  # half-life 60 draws
    for n in d["nums"]:
        wmain[n] += w
    wmb[d["mb"]] += w
exp_w = sum(0.5 ** ((len(last) - 1 - k) / 60.0) for k in range(len(last))) * PICK / MAIN_MAX
exp_wmb = sum(0.5 ** ((len(last) - 1 - k) / 60.0) for k in range(len(last))) / MB_MAX
hot = sorted(wmain, key=lambda i: -wmain[i])
print("Top 10 main (weighted): " + ", ".join(f"{i} ({wmain[i]:.1f})" for i in hot[:10]))
print("Bottom 5 main          : " + ", ".join(f"{i} ({wmain[i]:.1f})" for i in hot[-5:]))
hotmb = sorted(wmb, key=lambda i: -wmb[i])
print("Top 5 Mega Ball        : " + ", ".join(f"{i} ({wmb[i]:.1f})" for i in hotmb[:5]))

# --- overdue / delay ---
print()
print("=" * 72)
print("4. DELAY ANALYSIS (draws since last appearance - 'overdue')")
print("=" * 72)
latest = draws[-1]["draw"]
last_seen = {}
for d in draws:
    for n in d["nums"]:
        last_seen[n] = d["draw"]
    last_seen[("MB", d["mb"])] = d["draw"]
delay = {i: latest - last_seen.get(i, draws[0]["draw"]) for i in range(1, MAIN_MAX + 1)}
avg_gap = {i: N / main_c[i] for i in range(1, MAIN_MAX + 1)}
ratio = sorted(range(1, MAIN_MAX + 1), key=lambda i: -(delay[i] / avg_gap[i]))
print(f"{'Num':>4} {'Away':>6} {'AvgGap':>7} {'Ratio':>6}  status")
for i in ratio[:12]:
    status = "OVERDUE" if delay[i] / avg_gap[i] > 1.3 else ("fresh" if delay[i] < avg_gap[i] * 0.4 else "")
    print(f"{i:>4} {delay[i]:>6} {avg_gap[i]:>7.1f} {delay[i]/avg_gap[i]:>6.2f}  {status}")
mb_last = {i: latest - last_seen.get(("MB", i), draws[0]["draw"]) for i in range(1, MB_MAX + 1)}
mb_gap = {i: N / mb_c[i] for i in range(1, MB_MAX + 1)}
mbr = sorted(range(1, MB_MAX + 1), key=lambda i: -(mb_last[i] / mb_gap[i]))
print("Mega Ball overdue ratio: " + ", ".join(
    f"{i} ({mb_last[i]} away, {mb_last[i]/mb_gap[i]:.2f})" for i in mbr[:5]))

# --- pairs ---
print()
print("=" * 72)
print("5. MOST FREQUENT PAIRS")
print("=" * 72)
exp_pair = N * (PICK * (PICK - 1) / 2) / comb(MAIN_MAX, 2)
print(f"expected pairs to repeat: {exp_pair:.2f} times each")
for (a, b), c in pair_c.most_common(12):
    print(f"  {a:02d}-{b:02d}: {c} times ({c/exp_pair:.2f}x expected)")

# --- structural patterns ---
print()
print("=" * 72)
print("6. STRUCTURAL PATTERNS (odd/even, high/low, sum)")
print("=" * 72)
odd = Counter(sum(n % 2 for n in d["nums"]) for d in draws)
sums = [sum(d["nums"]) for d in draws]
print("Odd count in 4-number pick:")
for k in sorted(odd):
    p = comb(15, k) * comb(15, 4 - k) / comb(30, 4) if 0 <= 4 - k <= 15 else 0
    print(f"  {k} odd / {4-k} even: {odd[k]:>5} ({100*odd[k]/N:>5.1f}%)  expected {100*p:>5.1f}%")
print(f"Sum: min={min(sums)} max={max(sums)} mean={sum(sums)/N:.1f} median={sorted(sums)[N//2]}")
print(f"  (uniform 4-of-30 theoretical mean = {4*(MAIN_MAX+1)/2:.1f})")
low = Counter(sum(1 for n in d["nums"] if n <= 15) for d in draws)
print("Numbers in low half (1-15):")
for k in sorted(low):
    p = comb(15, k) * comb(15, 4 - k) / comb(30, 4) if 0 <= 4 - k <= 15 else 0
    print(f"  {k} low / {4-k} high: {low[k]:>5} ({100*low[k]/N:>5.1f}%)  expected {100*p:>5.1f}%")

# --- strategy scoring ---
print()
print("=" * 72)
print("7. COMPOSITE SCORE & RECOMMENDED PICKS")
print("=" * 72)
# Normalize full-history frequency, recency weight, and overdue ratio
freq_n = {i: main_c[i] / exp_main for i in range(1, MAIN_MAX + 1)}
rec_n = {i: wmain[i] / exp_w for i in range(1, MAIN_MAX + 1)}
od_n = {i: delay[i] / avg_gap[i] for i in range(1, MAIN_MAX + 1)}
score = {i: 0.4 * freq_n[i] + 0.4 * rec_n[i] + 0.2 * od_n[i] for i in range(1, MAIN_MAX + 1)}
top = sorted(score, key=lambda i: -score[i])
print(f"{'Num':>4} {'Freq':>6} {'Recent':>7} {'Overdue':>8} {'Score':>6}")
for i in top[:12]:
    print(f"{i:>4} {freq_n[i]:>6.2f} {rec_n[i]:>7.2f} {od_n[i]:>8.2f} {score[i]:>6.3f}")

mbscore = {}
for i in range(1, MB_MAX + 1):
    f = mb_c[i] / exp_mb
    r = wmb[i] / exp_wmb
    o = mb_last[i] / mb_gap[i]
    mbscore[i] = 0.4 * f + 0.4 * r + 0.2 * o
mbtop = sorted(mbscore, key=lambda i: -mbscore[i])
print("\nMega Ball score: " + ", ".join(f"{i}:{mbscore[i]:.2f}" for i in mbtop[:5]))

best4 = sorted(top[:4])
print()
print(f"*** BEST PICK (composite): {best4[0]:02d}-{best4[1]:02d}-{best4[2]:02d}-{best4[3]:02d}  + MB {mbtop[0]:02d} ***")
alt = sorted(top[4:8])
print(f"*** ALTERNATE  : {alt[0]:02d}-{alt[1]:02d}-{alt[2]:02d}-{alt[3]:02d}  + MB {mbtop[1]:02d} ***")
alt2 = sorted(top[8:12])
print(f"*** ALTERNATE  : {alt2[0]:02d}-{alt2[1]:02d}-{alt2[2]:02d}-{alt2[3]:02d}  + MB {mbtop[2]:02d} ***")

# --- honest probability statement ---
print()
print("=" * 72)
print("8. HONEST PROBABILITY CHECK")
print("=" * 72)
odds = comb(MAIN_MAX, PICK) * MB_MAX
print(f"Jackpot odds (4 of 30 + 1 of 15) = C(30,4) x 15 = {comb(MAIN_MAX,PICK)} x 15 = 1 in {odds:,}")
print("Every valid combination has EXACTLY the same 1-in-%d chance." % odds)
print(f"Chi-square tests above: main p={chi2_p(x2,MAIN_MAX-1):.3f}, mega p={chi2_p(x2m,MB_MAX-1):.3f}")
print("=> deviations are consistent with random noise; history cannot raise the odds.")

# best & worst combination historically (how often top picks appeared)
print()
print("How often did the actual top-4 frequent combo appear?")
top4set = tuple(sorted(main_c.most_common(4)[i][0] for i in range(4)))
print(f"  most frequent 4 individual numbers: {top4set}")
cc = Counter(tuple(d["nums"]) for d in draws)
for combo, cnt in cc.most_common(5):
    print(f"  {combo}: {cnt} times")
print(f"  distinct combos seen: {len(cc)} of {comb(MAIN_MAX, PICK):,} possible")
