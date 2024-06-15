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
    np.random.seed(0)
    weights = np.random.randn(X.shape[1], 1)  # 가중치
    bias = np.zeros((1, 1))  # 편향

    # 학습률 설정
    learning_rate = 0.01

    # 학습 횟수 설정
    epochs = 1000

    # 경사 하강법(Gradient Descent)을 사용하여 모델 학습
    for epoch in range(epochs):
        # 모델의 예측값 계산
        y_pred = np.dot(X_normalized, weights) + bias

        # 손실 함수 계산 (평균 제곱 오차)
        loss = np.mean((y_pred - y_normalized) ** 2)

        # 손실 함수에 대한 가중치와 편향의 편미분 계산
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

    # 예측 함수 정의
    def predict(X):
        X_normalized = (X - X_mean) / X_std  # 입력 데이터 정규화
        y_pred_normalized = np.dot(X_normalized, weights) + bias  # 모델 예측
        y_pred = y_pred_normalized * y_std + y_mean  # 정규화된 예측 결과를 원래 스케일로 변환
        return y_pred

    return predict

# 예시 데이터 생성
X_train = np.array([[1], [2], [3], [4], [5]])
y_train = np.array([[2], [4], [6], [8], [10]])

# 선형 회귀 모델 학습
predict_func = linear(X_train, y_train)

# 새로운 데이터에 대한 예측
X_test = np.array([[6], [7], [8]])
predictions = predict_func(X_test)
print("새로운 데이터에 대한 예측:")
print(predictions)
