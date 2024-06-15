import numpy as np


def linear(arr, res):
    # 입력 데이터 생성
    X = np.array(arr)

    # 정답 데이터 생성
    y = np.array(res)

    # 데이터 정규화
    X_mean = np.mean(X, axis=0)
    X_std = np.std(X, axis=0)
    y_mean = np.mean(y, axis=0)
    y_std = np.std(y, axis=0)

    X_std[X_std == 0] = 1e-8
    y_std[y_std == 0] = 1e-8

    X_normalized = (X - X_mean) / X_std
    y_normalized = (y - y_mean) / y_std

    # 모델 파라미터 초기화 (가중치와 편향)
    # 여기서는 가중치를 무작위로 초기화하고, 편향은 0으로 초기화합니다.
    np.random.seed(0)
    weights = np.random.randn(2, 1)  # 2개의 특성에 대한 가중치
    bias = np.zeros((1, 1))  # 편향

    # 학습률 설정
    learning_rate = 0.01

    # 학습 횟수 설정
    epochs = 1000

    # 경사 하강법(Gradient Descent)을 사용하여 모델 학습
    for epoch in range(epochs):
        # 모델의 예측값 계산
        # y_pred = np.dot(X, weights) + bias
        # 정규화 된 데이터 로 처리
        y_pred = np.dot(X_normalized, weights) + bias

        # 손실 함수 계산 (평균 제곱 오차)
        loss = np.mean((y_pred - y) ** 2)

        # 손실 함수에 대한 가중치와 편향의 편미분 계산
        # dw = np.dot(X.T, (y_pred - y)) / len(X)
        # db = np.mean(y_pred - y)
        # 정규화 된 데이터 로 처리
        dw = np.dot(X_normalized.T, (y_pred - y_normalized)) / len(X_normalized)
        db = np.mean(y_pred - y_normalized)

        # 가중치와 편향 업데이트
        weights -= learning_rate * dw
        bias -= learning_rate * db

        # 매 100번째 epoch마다 손실 출력
        if epoch % 100 == 0:
            print(f'Epoch {epoch}: Loss = {loss}')

    # 학습된 모델의 가중치와 편향 출력
    print("학습된 가중치(weights):")
    print(weights)
    print("학습된 편향(bias):")
    print(bias)

    # return [weights, bias, y_std, y_mean]
    # 예측 결과를 원래 스케일로 변환하여 반환
    def predict(X):
        X = (X - X_mean) / X_std  # 입력 데이터를 정규화
        y_pred_normalized = np.dot(X, weights) + bias  # 모델 예측
        y_pred = y_pred_normalized * y_std + y_mean  # 정규화된 예측 결과를 원래 스케일로 변환
        return y_pred
        # return y_pred_normalized

    return predict


def calc(weights, bias, data, predicted_data):
    new_data = np.array(data)

    print('new data : {}'.format(new_data))
    data_mean = np.mean(new_data, axis=0)
    data_std = np.std(new_data, axis=0)
    data_std[data_std == 0] = 1e-8

    data_normalized = (new_data - data_mean) / data_std

    # 추론
    predicted_price = np.dot(data_normalized, weights) + bias
    origin_predicted_price = predicted_price * predicted_data[0] + predicted_data[1]

    # 예측된 주택 가격 출력
    print("예측된 데이터:", origin_predicted_price[0][0])
    return origin_predicted_price


if __name__ == "__main__":
    test()