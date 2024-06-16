import array


class EmptyListWarning(Warning):
    def __init__(self):
        super().__init__('List is empty')


class NotSameShapeError(Exception):
    def __init__(self):
        super().__init__('The two lists are not in the same format')


def c_array(arr_list, dtype=None) -> array:
    arr = []
    define_type = dtype if dtype is not None else str if any(isinstance(item, str) for item in arr_list) else type(
        arr_list[0])

    for item in arr_list:
        arr.append(define_type(item))
    return arr


def c_shape(arr_list) -> array:
    if not isinstance(arr_list, list):
        return ()
    if len(arr_list) == 0:
        return 0
    first_element = arr_list[0]
    return (len(arr_list),) + c_shape(first_element)


def c_mean(arr_list: list, axis: int = None) -> array:
    mean_list = []
    if len(arr_list) == 0:
        raise EmptyListWarning

    arr_shape = c_shape(arr_list=arr_list)
    dimensional_arr = list_dimensional(arr_list)

    ele_total_size = 1
    for size in arr_shape:
        ele_total_size *= size
    ele_size_without_first = int(ele_total_size / arr_shape[0])

    if axis is None:
        return sum(dimensional_arr) / ele_total_size
    elif axis == 0:
        tmp_list = []
        for idx in range(0, ele_size_without_first):
            tmp_sum = 0
            for first_list_idx in range(0, arr_shape[0]):
                tmp_sum += dimensional_arr[idx + first_list_idx * ele_size_without_first]
            tmp_list.append(tmp_sum / arr_shape[0])
            if idx > 0 and (idx + 1) % arr_shape[-1] == 0:
                mean_list.append(tmp_list)
                tmp_list = []
    elif axis == 1:
        ele_list_size = int(ele_size_without_first / arr_shape[-1])
        for idx in range(0, arr_shape[0]):
            tmp_list = []
            for sub_idx in range(0, arr_shape[-1]):
                tmp_sum = 0
                for ele_idx in range(0, ele_list_size):
                    tmp_sum += dimensional_arr[ele_idx * arr_shape[-1] + sub_idx + idx * ele_size_without_first]
                tmp_list.append(tmp_sum / ele_list_size)
            mean_list.append(tmp_list)
    elif axis == 2:
        tmp_sum = 0
        tmp_list = []
        for idx in range(0, ele_total_size):
            tmp_sum += dimensional_arr[idx]
            if idx > 0 and (idx + 1) % arr_shape[-1] == 0:
                tmp_list.append(tmp_sum / arr_shape[-1])
                tmp_sum = 0
            if idx > 0 and (idx + 1) % ele_size_without_first == 0:
                mean_list.append(tmp_list)
                tmp_list = []
    return mean_list


def c_std(arr_list: list, axis: int = None):
    if len(arr_list) == 0:
        raise EmptyListWarning

    arr_shape = c_shape(arr_list)
    dimensional_arr = list_dimensional(arr_list)

    if axis is None:
        list_len = len(dimensional_arr)
        list_avg = sum(dimensional_arr) / list_len

        variance = sum((item - list_avg) ** 2 for item in dimensional_arr) / list_len
        std_value = variance ** 0.5
        return std_value
    elif axis == 0:
        std_list = []
        ele_total_size = len(dimensional_arr)
        ele_size_without_first = int(ele_total_size / arr_shape[0])
        ele_size_last = arr_shape[-1]

        tmp_list = []
        for idx in range(0, ele_size_without_first):
            tmp_sum = 0
            for sub_idx in range(0, arr_shape[0]):
                tmp_sum += dimensional_arr[sub_idx * ele_size_without_first + idx]
            tmp_avg = tmp_sum / arr_shape[0]
            tmp_std = (sum(
                (dimensional_arr[sub_idx * ele_size_without_first + idx] - tmp_avg) ** 2
                for sub_idx in range(0, arr_shape[0])
            ) / arr_shape[0]) ** 0.5
            tmp_list.append(tmp_std)
            if idx > 0 and (idx + 1) % ele_size_last == 0:
                std_list.append(tmp_list)
                tmp_list = []
        return std_list
    elif axis == 1:
        std_list = []
        ele_total_size = len(dimensional_arr)
        ele_size_without_first = int(ele_total_size / arr_shape[0])
        ele_size_last = arr_shape[-1]

        tmp_list = []
        for idx in range(0, arr_shape[0]):
            for sub_idx in range(0, ele_size_last):
                tmp_sum = 0
                repeat_size = int(ele_total_size / (arr_shape[0] * ele_size_last))
                for ele_idx in range(0, repeat_size):
                    tmp_sum += dimensional_arr[ele_idx * ele_size_last + sub_idx + idx * ele_size_without_first]
                tmp_avg = tmp_sum / repeat_size
                tmp_std = sum(
                    (dimensional_arr[ele_idx * ele_size_last + sub_idx + idx * ele_size_without_first] - tmp_avg) ** 2
                    for ele_idx in range(0, repeat_size)
                ) / repeat_size
                tmp_list.append(tmp_std ** 0.5)
            std_list.append(tmp_list)
            tmp_list = []
        return std_list


def c_dot(first_list: list, second_list: any):
    if isinstance(second_list, list):
        first_shape = c_shape(arr_list=first_list)
        second_shape = c_shape(arr_list=second_list)
        if first_shape[-1] != second_shape[0]:
            raise NotSameShapeError

        if len(first_shape) == len(second_shape) == 1:
            list_len = len(first_list)
            list_sum = sum((first_list[idx] * second_list[idx]) for idx in range(0, list_len))
            return list_sum
        else:
            dot_list = []
            tmp_list = []
            for x_idx in range(0, first_shape[0]):
                for sub_x_idx in range(0, second_shape[-1]):
                    tmp_sum = sum(
                        first_list[x_idx][y_idx] * second_list[y_idx][sub_x_idx] for y_idx in range(0, first_shape[-1])
                    )
                    tmp_list.append(tmp_sum)
                dot_list.append(tmp_list)
                tmp_list = []
            return dot_list
    else:
        dot_list = []
        for item in first_list:
            dot_list.append(second_list * item)
        return dot_list


def list_dimensional(arr_list: list):
    arr_shape = c_shape(arr_list)
    dimensional_arr = arr_list
    tmp_arr = []
    for idx in range(0, len(arr_shape) - 1):
        for item in dimensional_arr:
            tmp_arr.extend(item)
        dimensional_arr = tmp_arr
        tmp_arr = []
    return dimensional_arr


def list_minus(first_list: list, second_list: list):
    if len(first_list) != len(second_list):
        raise NotSameShapeError
    minus_list = [(first_list[idx][0] - second_list[idx][0]) for idx in range(len(first_list))]

    return minus_list


def list_squared(arr_list: list):
    squared_list = []
    list_shape = c_shape(arr_list=arr_list)

    if len(list_shape) == 1:
        for item in arr_list:
            squared_list.append(item*item)
        return squared_list
    else:
        for x_idx in range(list_shape[0]):
            tmp_list = []
            for y_idx in range(list_shape[-1]):
                tmp_list.append(arr_list[x_idx][y_idx] * arr_list[x_idx][y_idx])
            squared_list.append(tmp_list)
        return squared_list
