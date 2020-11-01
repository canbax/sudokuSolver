# SudokuSolver implemented in Java

Backtracking algorithm vs Norvig algorithm which is implemented here http://norvig.com/sudoku.html in python.

I runned both algos with hardest 95 sudoku puzzles. 

This is the result
> norvig:87 backtracking: 5
> total time (ms):352  vs   55882

Norvig algorithm is most of the time faster than backtracking. In `search()` function of norvig, every recursive call must be called with deep copy of values.
Every recursive call must use its own independent parameter. (`HashMap<String, String> values`)
