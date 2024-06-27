from board_helpers import Board, Tile # pyright: ignore

# from_min = 60, from_max = 510
def map_value(value, from_min, from_max, to_min, to_max):
    ratio = (value - from_min) / (from_max - from_min)

    mapped_value = to_min + ratio * (to_max - to_min)

    return mapped_value

def heuristic_distance(node):
    positions = node.board.get_tiles(node.turn)

    sum = 0

    for p in positions:
        row, col = p
        
        sum += row
        sum += col

    return -1 * map_value(sum, 60, 510, -5, 5) # -5 * -1 = 5, so left upper best for white etc

def heuristic_turn(node):
    destined_tile = ()

    if node.turn == Tile.WHITE:
        destined_tile = (0, 0)
    else:
        destined_tile = (15, 15)

    destined_row, destined_col = destined_tile

    positions = node.board.get_tiles(node.turn)

    summed_turns = 0

    for p in positions:
        row, col = p

        needed_rows, needed_cols = abs(destined_row - row), abs(destined_col - col)
        
        turns = max(needed_rows, needed_cols) # because best move is 1,1 (-1,1)

        summed_turns += turns
    
    if node.turn == Tile.WHITE:
        return map_value(summed_turns, 0, 272, 0, 5)
    else:
        return -1 * map_value(summed_turns, 0, 272, 0, 5)

WEIGHT_BOARD = [
    [3.0, 2.8, 2.6, 2.4, 2.2, 2.0, 1.8, 1.6, 1.4, 1.2, 1.0, 0.8, 0.6, 0.4, 0.2, 0],
    [2.8, 2.6, 2.4, 2.2, 2.0, 1.8, 1.6, 1.4, 1.2, 1.0, 0.8, 0.6, 0.4, 0.2, 0, -0.2],
    [2.6, 2.4, 2.2, 2.0, 1.8, 1.6, 1.4, 1.2, 1.0, 0.8, 0.6, 0.4, 0.2, 0, -0.2, -0.4],
    [2.4, 2.2, 2.0, 1.8, 1.6, 1.4, 1.2, 1.0, 0.8, 0.6, 0.4, 0.2, 0, -0.2, -0.4, -0.6],
    [2.2, 2.0, 1.8, 1.6, 1.4, 1.2, 1.0, 0.8, 0.6, 0.4, 0.2, 0, -0.2, -0.4, -0.6, -0.8],
    [2.0, 1.8, 1.6, 1.4, 1.2, 1.0, 0.8, 0.6, 0.4, 0.2, 0, -0.2, -0.4, -0.6, -0.8, -1.0],
    [1.8, 1.6, 1.4, 1.2, 1.0, 0.8, 0.6, 0.4, 0.2, 0, -0.2, -0.4, -0.6, -0.8, -1.0, -1.2],
    [1.6, 1.4, 1.2, 1.0, 0.8, 0.6, 0.4, 0.2, 0, -0.2, -0.4, -0.6, -0.8, -1.0, -1.2, -1.4],
    [1.4, 1.2, 1.0, 0.8, 0.6, 0.4, 0.2, 0, -0.2, -0.4, -0.6, -0.8, -1.0, -1.2, -1.4, -1.6],
    [1.2, 1.0, 1.8, 0.6, 0.4, 0.2, 0, -0.2, -0.4, -0.6, -0.8, -1.0, -1.2, -1.4, -1.6, -1.8],
    [1.0, 0.8, 1.6, 0.4, 0.2, 0, -0.2, -0.4, -0.6, -0.8, -1.0, -1.2, -1.4, -1.6, -1.8, -2.0],
    [0.8, 0.6, 0.4, 0.2, 0, -0.2, -0.4, -0.6, -0.8, -1.0, -1.2, -1.4, -1.6, -1.8, -2.0, -2.2],
    [0.6, 0.4, 0.2, 0, -0.2, -0.4, -0.6, -0.8, -1.0, -1.2, -1.4, -1.6, -1.8, -2.0, -2.2, -2.4],
    [0.4, 0.2, 0, -0.2, -0.4, -0.6, -0.8, -1.0, -1.2, -1.4, -1.6, -1.8, -2.0, -2.2, -2.4, -2.6],
    [0.2, 0, -0.2, -0.4, -0.6, -0.8, -1.0, -1.2, -1.4, -1.6, -1.8, -2.0, -2.2, -2.4, -2.6, -2.8],
    [0, -0.2, -0.4, -0.6, -0.8, -1.0, -1.2, -1.4, -1.6, -1.8, -2.0, -2.2, -2.4, -2.6, -2.8, -3.0]
]

def heuristic_weight(node):
    positions = node.board.get_tiles(node.turn)

    weight = 0

    for p in positions:
        row, col = p

        weight += WEIGHT_BOARD[row][col]

    return map_value(weight, -45, 45, -5, 5)
