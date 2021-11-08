import matplotlib.pyplot as plt

def get_points(x1 = 0, y1 = 0, x2 = 0, y2 = 0):
    result = []
    dx = x2 - x1
    dy = y2 - y1

    sign_x = 1 if dx > 0 else -1 if dx < 0 else 0
    sign_y = 1 if dy > 0 else -1 if dy < 0 else 0

    if dx < 0: dx = -dx
    if dy < 0: dy = -dy

    if dx > dy:
        pdx, pdy = sign_x, 0
        es, el = dy, dx
    else:
        pdx, pdy = 0, sign_y
        es, el = dx, dy

    x, y = x1, y1
    error, t = el/2, 0
    result.append((x,y))

    while t < el:
        error -= es
        if error < 0:
            error += el
            x += sign_x
            y += sign_y
        else:
            x += pdx
            y += pdy
        # Меньше шаг - больше точек
        t += 0.1
        result.append((x,y))
    return result

def draw_line(points):
    for point in points:
        plt.plot(point[0], point[1], 'ro')

points = get_points(0, 0, 10, 10)
draw_line(points)