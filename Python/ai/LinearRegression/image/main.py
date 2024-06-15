import matplotlib.pyplot as plt

import ImagePillow as ip
import LinearRegression as lr

plt.style.use('_mpl-gallery')

r_array = []
g_array = []
b_array = []

y_r_array = []
y_g_array = []
y_b_array = []

width = 0
height = 0

for i in range(0, 129):
    img_name = str(i)+'.png'
    image_arr = ip.image_info(img_name)
    pixel_array = image_arr[0]
    width = image_arr[1]
    height = image_arr[2]

    for pa in range(0, len(pixel_array)):
        pixel = pixel_array[pa]
        for p in range(0, len(pixel)):
            img = pixel[p]
            r_array.append([i, pa * width + p])
            g_array.append([i, pa * width + p])
            b_array.append([i, pa * width + p])

            y_r_array.append([float(img[0])])
            y_g_array.append([float(img[1])])
            y_b_array.append([float(img[2])])

red_func = lr.linear(r_array, y_r_array)
green_func = lr.linear(g_array, y_g_array)
blue_func = lr.linear(b_array, y_b_array)

red_arr = []
green_arr = []
blue_arr = []
index_arr = []
count = 0

target_list = [0, 50, 100, 200, 250]
for num in target_list:
    index_arr.append(count)
    red_pred = red_func([num, i])
    green_pred = green_func([num, i])
    blue_pred = blue_func([num, i])
    count += 1
    print('result {} - red : {}, green : {}, blue : {}'.format(num, red_pred, green_pred, blue_pred))
    ip.make_image_one_color(color=(int(red_pred[0][0]), int(green_pred[0][0]), int(blue_pred[0][0])), width=width, height=height)

