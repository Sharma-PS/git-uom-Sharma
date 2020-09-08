
/**
 *
 * @author SarvesWaran
 */
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    // Length of the square grid "gLen * gLen"
    private int gLen;

    // Array representing indexes of all sites (either it"s open or blocked)
    private boolean[] sites;

    // Number of open sites
    private int oSNum;

    // Index of the top virtual site (has value 0)
    private int vTopIdx;

    // Index of the top virtual site (has value (gLen * gLen) + 1)
    private int vBotIdx;

    // Weighted quick union-find data structure
    // to calculate percolation
    private WeightedQuickUnionUF ufForPercolation;

    // Weighted quick union-find data structure
    // to calculate fullness (without bottom virtual site)
    private WeightedQuickUnionUF ufForFullness;

    // Create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("Grid must have at least one row and column");
        }

        gLen = n;
        int gridSize = (n * n) + 2; // with two virtual sites
        sites = new boolean[gridSize];
        oSNum = 0;

        // init and open virtual sites
        vTopIdx = 0;
        vBotIdx = (gLen * gLen) + 1;
        sites[vTopIdx] = true;
        sites[vBotIdx] = false;

        ufForPercolation = new WeightedQuickUnionUF(gridSize);
        ufForFullness = new WeightedQuickUnionUF(gridSize);

        // connect top and bottom rows to virtual sites
        for (int col = 1; col <= gLen; col++) {
            int rowTop = 1;
            int sTopIdx = getIndexByRandC(rowTop, col);
            ufForPercolation.union(vTopIdx, sTopIdx);
            ufForFullness.union(vTopIdx, sTopIdx);

            int rowBottom = gLen;
            int sBotIdx = getIndexByRandC(rowBottom, col);
            ufForPercolation.union(vBotIdx, sBotIdx);
        }
    }

    // Open site (row, col) if it is not open already
    public void open(int row, int col)
    {
        int siteIndex = getIndexByRandC(row, col);
        if (sites[siteIndex]) {
            return;
        }

        oSNum++;
        sites[siteIndex] = true;

        // connect with left neighbor
        if (col > 1 && isOpen(row, col - 1)) {
            int siteLeftIndex = getIndexByRandC(row, col - 1);
            ufForPercolation.union(siteIndex, siteLeftIndex);
            ufForFullness.union(siteIndex, siteLeftIndex);
        }

        // connect with right neighbor
        if (col < gLen && isOpen(row, col + 1)) {
            int siteLeftIndex = getIndexByRandC(row, col + 1);
            ufForPercolation.union(siteIndex, siteLeftIndex);
            ufForFullness.union(siteIndex, siteLeftIndex);
        }

        // connect with top neighbor
        if (row > 1 && isOpen(row - 1, col)) {
            int siteLeftIndex = getIndexByRandC(row - 1, col);
            ufForPercolation.union(siteIndex, siteLeftIndex);
            ufForFullness.union(siteIndex, siteLeftIndex);
        }

        // connect with bottom neighbor
        if (row < gLen && isOpen(row + 1, col)) {
            int siteLeftIndex = getIndexByRandC(row + 1, col);
            ufForPercolation.union(siteIndex, siteLeftIndex);
            ufForFullness.union(siteIndex, siteLeftIndex);
        }
    }

    // If site (row, col) open
    public boolean isOpen(int row, int col)
    {
        int siteIndex = getIndexByRandC(row, col);

        return sites[siteIndex];
    }

    // If site (row, col) full
    public boolean isFull(int row, int col)
    {
        int siteIndex = getIndexByRandC(row, col);

        return (isOpen(row, col) && ufForFullness.connected(vTopIdx, siteIndex));
    }

    // Number of open sites
    public int numberOfOpenSites()
    {
        return oSNum;
    }

    // If the system percolate
    public boolean percolates()
    {
        // if grid with one site - check if it"s open
        if (gLen == 1) {
            int siteIndex = getIndexByRandC(1, 1);
            return sites[siteIndex];
        }

        return ufForPercolation.connected(vTopIdx, vBotIdx);
    }

    // Get site"s index to be represented in array
    private int getIndexByRandC(int row, int col)
    {
        validateBounds(row, col);

        return ((row - 1) * gLen) + col;
    }

    // Check if row and column values are in range of grid size
    private void validateBounds(int row, int col)
    {
        if (row > gLen || row < 1) {
            throw new IndexOutOfBoundsException("Row index is out of bounds");
        }

        if (col > gLen || col < 1) {
            throw new IndexOutOfBoundsException("Column index is out of bounds");
        }
    }
    
}
