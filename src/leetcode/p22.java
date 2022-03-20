package leetcode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
public class p22{
    static class s2200{
        public List<Integer> findKDistantIndices(int[] a, int key, int k){
            List<Integer> r = new ArrayList<>();
            for(int i = 0, prev = 0; i < a.length; i++)
                if(a[i] == key){
                    for(int j = Math.max(i - k, prev); j < a.length && j <= i + k; j++)
                        r.add(j);
                    prev = i + k + 1;
                }
            return r;
        }
    }

    static class s2201{//Count Artifacts That Can Be Extracted
        public int digArtifacts(int n, int[][] artifacts, int[][] dig){
            Set<Integer> s = Arrays.stream(dig).map(d -> d[0] * 1000 + d[1]).collect(Collectors.toSet());
            return (int) Arrays.stream(artifacts).filter(a -> uncovered(a[0], a[1], a[2], a[3], s)).count();
        }

        boolean uncovered(int r1, int c1, int r2, int c2, Set<Integer> s){
            for(int i = r1; i <= r2; i++)
                for(int j = c1; j <= c2; j++)
                    if(!s.contains(1000 * i + j))
                        return false;
            return true;
        }
    }

    static class s2202{//Maximize the Topmost Element After K Moves
        public int maximumTop(int[] a, int k){
            if(a.length == 1 && k % 2 == 1)
                return -1;
            if(k > a.length)
                return Arrays.stream(a).max().getAsInt();
            PriorityQueue<Integer> q = new PriorityQueue<>(Comparator.reverseOrder());
            IntStream.range(0, k - 1).forEach(i -> q.offer(a[i]));
            int r = !q.isEmpty() ? q.poll() : -1;
            return k < a.length ? Math.max(r, a[k]) : r;
        }
    }

    static class s2203{//Minimum Weighted Subgraph With the Required Paths
        public long minimumWeight(int n, int[][] edges, int src1, int src2, int dest){
            List<Map<Integer, Integer>> g = IntStream.range(0, n).mapToObj(i -> new HashMap<Integer, Integer>()).collect(Collectors.toList());
            List<Map<Integer, Integer>> g1 = IntStream.range(0, n).mapToObj(i -> new HashMap<Integer, Integer>()).collect(Collectors.toList());
            for(int[] e : edges){
                g.get(e[0]).put(e[1], Math.min(e[2], g.get(e[0]).getOrDefault(e[1], Integer.MAX_VALUE)));
                g1.get(e[1]).put(e[0], Math.min(e[2], g1.get(e[1]).getOrDefault(e[0], Integer.MAX_VALUE)));
            }
            long a[] = dfs(src1, g), b[] = dfs(src2, g), c[] = dfs(dest, g1), r = Long.MAX_VALUE;
            for(int i = 0; i < n; i++)
                if(a[i] != Long.MAX_VALUE && b[i] != Long.MAX_VALUE && c[i] != Long.MAX_VALUE)
                    r = Math.min(r, a[i] + b[i] + c[i]);
            return r == Long.MAX_VALUE ? -1 : r;
        }

        long[] dfs(int start, List<Map<Integer, Integer>> g){
            long[] dist = new long[g.size()];
            Arrays.fill(dist, Long.MAX_VALUE);
            PriorityQueue<long[]> q = new PriorityQueue<>(Comparator.comparingLong(a -> a[0]));
            dist[start] = 0;
            for(q.offer(new long[]{0, start}); !q.isEmpty(); ){
                long p[] = q.poll(), d = p[0], u = p[1];
                Map<Integer, Integer> adj = g.get((int) u);
                for(int v : adj.keySet())
                    if(d + adj.get(v) < dist[v]){
                        dist[v] = d + adj.get(v);
                        q.offer(new long[]{dist[v], v});
                    }
            }
            return dist;
        }
    }

    static class s2204{//Distance to a Cycle in Undirected Graph
        public int[] distanceToCycle(int n, int[][] edges){
            int[] degree = new int[n], r = new int[n];
            List<List<Integer>> g = IntStream.range(0, n).mapToObj(i -> new ArrayList<Integer>()).collect(Collectors.toList());
            for(int[] e : edges){
                degree[e[0]]++;
                degree[e[1]]++;
                g.get(e[0]).add(e[1]);
                g.get(e[1]).add(e[0]);
            }
            Queue<Integer> q = IntStream.range(0, n).filter(u -> degree[u] == 1).boxed().collect(Collectors.toCollection(LinkedList::new));
            while(!q.isEmpty()){
                degree[q.peek()]--;
                g.get(q.poll()).stream().filter(u -> degree[u] > 0 && --degree[u] == 1).forEach(q::add);
            }
            boolean[] seen = new boolean[n];
            IntStream.range(0, n).filter(u -> degree[u] > 0).forEach(u -> {
                seen[u] = true;
                q.offer(u);
            });
            for(int d = 1; !q.isEmpty(); d++)
                for(int size = q.size(); size > 0; size--)
                    for(Integer v : g.get(q.poll()))
                        if(!seen[v]){
                            seen[v] = true;
                            r[v] = d;
                            q.offer(v);
                        }
            return r;
        }
    }

    static class s2206{//Divide Array Into Equal Pairs
        public boolean divideArray(int[] a){
            int[] f = new int[501];
            Arrays.stream(a).forEach(n -> f[n]++);
            return Arrays.stream(f).allMatch(n -> n % 2 == 0);
        }
    }

    static class s2207{//Maximize Number of Subsequences in a String
        public long maximumSubsequenceCount(String text, String pattern){
            char c1 = pattern.charAt(0), c2 = pattern.charAt(1);
            return Math.max(count(c1, c2, text, 0, 1), count(c2, c1, text, text.length() - 1, -1));
        }

        long count(char c1, char c2, String text, int start, int step){
            long r = 0, c1Count = 0;
            for(int i = start; 0 <= i && i < text.length(); i += step){
                c1Count += text.charAt(i) == c1 ? 1 : 0;
                r += text.charAt(i) == c2 ? c1Count : 0;
            }
            return r + (c1 != c2 ? c1Count : 0);
        }
    }

    static class s2208{//Minimum Operations to Halve Array Sum
        public int halveArray(int[] a){
            double originSum = Arrays.stream(a).asLongStream().sum(), sum = originSum;
            PriorityQueue<Double> q = new PriorityQueue<>(Comparator.reverseOrder());
            Arrays.stream(a).forEach(n -> q.offer(1.0 * n));
            int r = 0;
            while(sum * 2 > originSum){
                Double n = q.poll();
                sum = sum - n + n / 2;
                q.offer(n / 2);
                r++;
            }
            return r;
        }
    }

    static class s2209{//Minimum White Tiles After Covering With Carpets
        public int minimumWhiteTiles(String floor, int nCarpets, int carpetLen){
            int[][] dp = new int[floor.length() + 1][nCarpets + 1];
            for(int i = 1; i <= floor.length(); i++)
                for(int c = 0; c <= nCarpets; c++){
                    int skip = dp[i - 1][c] + floor.charAt(i - 1) - '0';
                    int cover = c > 0 ? dp[Math.max(0, i - carpetLen)][c - 1] : floor.length();
                    dp[i][c] = Math.min(skip, cover);
                }
            return dp[floor.length()][nCarpets];
        }
    }

    static class s2210{//Count Hills and Valleys in an Array
        public int countHillValley(int[] a){
            int r = 0, left = a[0];
            for(int i = 1; i < a.length - 1; i++)
                if(left < a[i] && a[i] > a[i + 1] || left > a[i] && a[i] < a[i + 1]){
                    r++;
                    left = a[i];
                }
            return r;
        }
    }

    static class s2211{//Count Collisions on a Road
        public int countCollisions(String dir){
            int r = 0, i = 0, right = 0;
            for(; i < dir.length() && dir.charAt(i) == 'L'; i++) ;
            for(; i < dir.length(); i++)
                if(dir.charAt(i) == 'R')
                    right++;
                else{
                    r += dir.charAt(i) == 'S' ? right : right + 1;
                    right = 0;
                }
            return r;
        }
    }

    static class s2212{//Maximum Points in an Archery Competition
        int r[] = new int[12], maxPoints = 0;
        public int[] maximumBobPoints(int numArrows, int[] aliceArrows){
            bt(0, numArrows, aliceArrows, new int[aliceArrows.length], 0);
            return r;
        }

        void bt(int i, int numArrows, int[] aliceArrows, int[] bobArrows, int points){
            if(i >= aliceArrows.length){
                if(points > maxPoints){
                    maxPoints = points;
                    r = bobArrows.clone();
                    r[0] += numArrows;
                }
            }else{
                if(numArrows > aliceArrows[i]){
                    bobArrows[i] = aliceArrows[i] + 1;
                    bt(i + 1, numArrows - bobArrows[i], aliceArrows, bobArrows, points + i);
                    bobArrows[i] = 0;
                }
                bt(i + 1, numArrows, aliceArrows, bobArrows, points);
            }
        }
    }
}
