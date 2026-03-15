# placeOrder 경계값 분석(BVA) 테스트 시나리오

## 1) 테스트 대상
- 메서드: `OrderServiceImpl.placeOrder(OrderCreateRequest request)`
- 핵심 경계 변수:
  - 주문 수량(request.quantity)
  - 현재 재고(stock.quantity)

## 2) 경계값 정의
- 주문 수량의 유효 최소값: 1
- 재고 성공 경계: `stock.quantity == request.quantity`
- 재고 실패 경계: `stock.quantity == request.quantity - 1`

## 3) BVA 시나리오
| 시나리오 ID | 현재 재고 | 주문 수량 | 경계 유형 | 기대 결과 |
|---|---:|---:|---|---|
| BVA-01 | 1 | 1 | 성공 경계(동일값) | 주문 성공, 재고 0, 주문 저장 |
| BVA-02 | 0 | 1 | 실패 경계(경계 바로 아래) | `InsufficientStockException`, 주문 미저장 |
| BVA-03 | 2 | 1 | 경계 상단 내부값 | 주문 성공, 재고 1 |
| BVA-04 | 1 | 2 | 경계 하단 외부값 | `InsufficientStockException` |
| BVA-05 | 1 | 0 | 입력 유효성 경계(최소 미만) | 컨트롤러 레벨에서 검증 실패(400) |

## 4) 이번 단위 테스트 범위
- 포함: BVA-01, BVA-02
- 제외: BVA-03~05 (후속 테스트 권장)