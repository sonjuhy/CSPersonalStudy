from PIL import Image
from pathlib import Path

BASE_DIR = Path(__file__).resolve().parent.parent


def image_info(name):
    image_path = BASE_DIR / 'img' / 'sample' / name
    image = Image.open(image_path)

    # 이미지를 RGB 모드로 변환 (이미지 모드를 확인하고 필요에 따라 변환할 수 있음)
    image = image.convert("RGB")

    # 이미지의 너비와 높이 가져오기
    width, height = image.size
    # print('image width : {}, height : {}, multi : {}'.format(width, height, (width*height)))

    # 이미지의 픽셀 데이터 가져오기
    pixel_data = list(image.getdata())

    # 픽셀 데이터를 2차원 배열로 변환 (너비와 높이에 맞춰서)
    pixel_array = [pixel_data[i * width:(i + 1) * width] for i in range(height)]
    # print(pixel_array[100])
    # 픽셀 배열 출력
    # print(pixel_array)
    return [pixel_array, width, height]


def make_image(r_array, b_array, g_array, width, height):
    pixel_array = []
    array_len = width * height
    for i in range(array_len):
        pixel_array.append([0, 0, 0])

    new_image = Image.new('RGB', (width, height))
    for y in range(height):
        for x in range(width):
            new_image.putpixel((x, y), [r_array[y*width+x], b_array[y*width+x], g_array[y*width+x]])
    new_image.show()


def make_image_one_color(color, width, height):
    new_image = Image.new('RGB', (50, 50), color=color)
    new_image.show()


def make_default_img():
    for i in range(0, 256):
        new_image = Image.new('RGB', (50, 50), color=(i, i, i))
        new_image.save(BASE_DIR / 'img' / 'sample' / (str(i) + '.png'), 'png')
# data = np.zeros([32,32,3], dtype=np.uint8)
# #black img
# black_img = Image.fromarray(data, 'RGB')
# black_img.show()


if __name__ == '__main__':
    make_default_img()
