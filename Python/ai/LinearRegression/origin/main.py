import custom_np as cnp
import numpy as np


def test_array():
    print('test c_array fun')
    test_arr_1 = [1, 2, 1.5]
    test_arr_2 = [1, '2', '3']
    print(cnp.c_array(test_arr_1))
    print(cnp.c_array(test_arr_2))
    print(cnp.c_array(test_arr_1, dtype=float))
    print(cnp.c_array(test_arr_2, dtype=int))


def test_shape():
    print('test c_shape fun')
    arr = [[[1, 2, 3, 4],
            [5, 6, 7, 8],
            [9, 10, 11, 12]],
           [[1, 2, 3, 4],
            [5, 6, 7, 8],
            [9, 10, 11, 12]]]
    print(cnp.c_shape(arr))


def test_mean():
    print('test c_mean fun')
    arr = [[[1, 2, 3, 4],
            [5, 6, 7, 8],
            [9, 10, 11, 12]],
           [[1, 2, 3, 4],
            [5, 6, 7, 8],
            [9, 10, 11, 12]]]
    print('custom np')
    print(cnp.c_mean(arr))
    for axis_idx in range(0, 3):
        print(cnp.c_mean(arr, axis=axis_idx))

    print('origin np')
    print(np.mean(np.array(arr)))
    for axis_idx in range(0, 3):
        result = np.mean(np.array(arr), axis=axis_idx)
        print(result)


def test_std():
    arr = [[[1, 2, 3, 4],
            [5, 6, 7, 8],
            [9, 10, 11, 12]],
           [[10, 20, 3, 4],
            [50, 60, 7, 8],
            [90, 100, 11, 12]]]
    # arr = [[1, 2, 3], [4, 5, 6], [7, 8, 9]]
    print(np.std(np.array(arr)))
    print(np.std(np.array(arr), axis=0))
    print(np.std(np.array(arr), axis=1))
    print()
    print(cnp.c_std(arr))
    print(cnp.c_std(arr, axis=0))
    print(cnp.c_std(arr, axis=1))


def test_dot():
    arr1 = [[1, 2], [3, 4]]
    arr2 = [[5, 6], [7, 8]]
    np_arr1 = np.array(arr1)
    np_arr2 = np.array(arr2)
    print(np.dot(np_arr1, np_arr2))
    print(cnp.c_dot(arr1, arr2))


if __name__ == "__main__":
    print('custom np function test')
    test_array()
    test_shape()
    test_mean()
    test_std()
    test_dot()
