#include <stdio.h>

int N = 8, ans = 0;
int row[8], col[8];
	int d[2][8 + 8 - 1];

void printBoard()
{
    int i, j;
    for (i = 0; i < N; i++)
    {
        for (j = 0; j < N; j++)
        {
            if (col[i] == j)
                printf(" O");
            else
                printf(" .");
        }
        printf("\n");
    }
    printf("\n");
}

void search(int c)
{
    // printf("%d\n", c);
    if (c == N)
    {
        ans = ans + 1;
        // printBoard();
    }
    else
    {
        int r;

        for (r = 0; r < N; r++)
        {
            if (row[r] == 0 && d[0][r + c] == 0 && d[1][r + N - 1 - c] == 0)
            {
                // printf("aaa\n");
                row[r] = d[0][r + c] = d[1][r + N - 1 - c] = 1;
                col[c] = r;
                search(c + 1);
                row[r] = d[0][r + c] = d[1][r + N - 1 - c] = 0;
            }
        }
    }
}

int main()
{

	// int r = 0, c = 0;
	d[1][7];
    // // row[0] == 0;
    search(0);
    printf("%d\n", ans);
    return 0;
}

