# **ReactiveX**
## I. Overview
	1 thư viện cho phép xử lý bất đồng bộ (Asynchronous) và event-based programs
	bằng các sử dụng các dãy obeservable.

- **Obeserver pattern**:
	- Hỗ trợ sequences data và events.
	- Thêm các operator để thao tác với chúng.
	- Xử lý đa luồng. 

> Có chỗ gọi là "Functional reactive programming" -> Sai. RX có thể functional và reactive nhưng FRP là 1 hình thức khác. Điểm khác nhau lớn nhất là FRP thao tác trên các giá trị *thay đổi liên tục* theo thời gian còn RX thao tác trên 1 giá trị *cố định* được phát ra theo thời gian.

## II. Observable
### 1. Definition
	Khác với lập trình thông thường, RX cho phép các task thực thi song song và kết quả sẽ trả về sau
	cho các "Observer". Ta cần định nghĩa 1 cơ chế để nhận và thay đổi dữ liệu => "Observable" và rồi 
	subcribe (đăng ký) "Observer" cho dữ liệu đó.
### 2. Why?
	RX Observable model cho phép bạn xử lý streams of async events với các operation đơn giản, linh hoạt
	khiến việc sử dụng tập dữ liệu, items như các array.
- Ưu điểm:
	- Thoát khỏi đống Callback lộn xộn.
	- Code dễ đọc hơn.
	- Giảm bug. 

### 3. Mechanism (cơ chế)

|   |Cách gọi bình thường | Trong Asynchronous model 
| --- | ------------------- | -------------------------
| 1 | Gọi 1 method 		   | 	Định nghĩa 1 method để thao tác với return value từ async call (trong Observer)              |
|2 | Lưu return value của method vào 1 biến | Định nghĩa 1 async call tự gọi chính nó (Observable)
|3| Dùng biến đó để làm gì đó | Gắn Observer vào Observable bằng cách Subcribe (đăng ký) 
|4| | Hoàn thành nốt các thao tác. khi có giá trị trả về (phát ra từ Observable) thì method của Observer sẽ được thực thi

- **onNext():**
	- Observable gọi onNext() mỗi khi nó emit 1 item. Method này nhận param là 1 item được emit 
- **onError():**
	- Method để báo hiệu việc tạo data đã bị lỗi hoặc thất bại. Nó sẽ không được gọi tới onNext() hay onCompleted(). Param là throwable chỉ lỗi.
- o**nCompleted():**
	- Được call sau onNext() cuối cùng nếu không gặp bất cứ lỗi nào.
	
>3 method trên nằm trong Subcribe() method, định nghĩa cách kết nối giữa Observer và Observable.

##III. Operators

>Operators (Toán tử) trong RX là tập hợp các method được cung cấp sẵn phục vụ cho các usercase khác nhau

### 1.Why?

>Với 1 lượng lớn các operators, bằng cách kết hợp chúng theo nhiều cách khác nhau; ta có thể thực thi được
	mọi thứ mà ta muốn.

### 2. Một số operators cơ bản
- Creating
	- `Create()`: Tạo 1 Observable.
	- `Defer()`: Tạo 1 Observable khi có subcribe cho mỗi Observer.
	- `Just()`: Convert 1 object hoặc set object thành 1 Observable phát chúng.
	- `From()`: Convert 1 hoặc vài object hoặc cấu trúc dữ liệu thành Observable.
- Transorming:
	- `Map()`: Transform item được phát từ Observable bằng 1 function áp dụng với mỗi item.
	- `flatMap()`: Transform item emit từ Observable thành các Observables, sau đó ép các item phát ra từ chúng lại thành 1 single Observable mới.
- Filtering:
	- `Filter()`: Chỉ emit các item thoả mãn điều kiện.
- Combining:
	- `Merge()`: Combine nhiều Observables thành 1 bằng cách hợp nhất các emissions.
	- `Zip()`: Combine emissions từ nhiều Observables bằng 1 function cụ thể và emit từ item cho mỗi combination dựa trên kết quả của function.
- [Many more...](https://reactivex.io/documentation/operators.html)

>Còn rất nhiều các operators khác phục vụ đủ các Usecase. Ta có thể research nó trên document của [ReactiveX](https://reactivex.io/documentation/operators.html). Trong đấy cũng đã có sẵn 1 decision tree cụ thể để hỗ trợ user quyết định sử dụng operator nào tuỳ theo từng trường hợp.

## IV.Single

>RxJava phát triển 1 biến thể của Observable gọi là `Single`.

- Single giống như 1 Observable, nhưng thay vì emit ra 1 loạt các giá trị (Từ không có gì tới vô cùng) thì Single luôn emit 1 giá trị hoặc 1 thông báo lỗi.
- Vì vậy, thay vì subcribe với 3 methods để phản hồi thông báo từ Observable như ở trên, Single chỉ sử dụng 2 method:
	- `onSuccess()`: Truyền vào item được emit.
	- `onError()`: truyền vào 1 Throwable() khiến Single không emit được item.
>Giống như Observable, Single cũng có thể chỉnh sửa bằng đa dạng các Operators. Có cả các Operator cho phép tương tác với cả Observable và Single nên ta có thể mix chúng với nhau tuỳ theo ý định.

## V. Subject
### 1. Definition
> Một Subject là 1 dạng cầu nối hoặc proxy khả dụng trong vài implementations của RX trong đó nó vừa là Observers, vừa là Observable.

- Vì nó là Observer => Nó có thể subcribe tới 1 hoăc nhiều Observable.
- Vì nó là Observable => Nó cũng có thể truyền các item nó observe bằng các reemit chúng, và có cả emit item mới.

```
Bởi vì Subject có thể subcribe tới 1 Observable, nghĩa là nó sẽ khiến Observable bắt đầu phát các items 
và nếu Observable ấy là "Cold" (Phải đợi có subcribe trước khi emit item). Điều này có thể có tác dụng
làm cho Subject trở thành 1 bản "Hot" của Observable ban đầu.
```
- Cold stream: Code sẽ được thực thi khi có ít nhất 1 single Observer.
- Hot stream: Code được thực thi và value được trả về kể cả khi không có Observer nào.
### 2. Các loại Subject

#### 1. AsyncSubject
- 1 AsyncSubject chỉ emit value cuối cùng được emit bới Observable gốc và đã completed.
![](https://reactivex.io/documentation/operators/images/S.AsyncSubject.png)
- Nếu có lỗi, AsyncSubject sẽ không emit gì mà sẽ pass thông báo lỗi từ source Observable.
![](https://reactivex.io/documentation/operators/images/S.AsyncSubject.e.png)

### 2. BehaviourSubject
- Khi có 1 Observer subcribe tới 1 BehaviourSubject, nó sẽ bắt đầu emit item gần nhất được emit từ source Observable (hoặc giá trị mặc định nếu chưa emit item nào) rối tiếp tục emit các item được emit sau đó bởi source Observable.
![](https://reactivex.io/documentation/operators/images/S.BehaviorSubject.png)
- Nếu có lỗi, BehaviourSubject sẽ không emit gì mà pass thông báo lỗi từ source Observable.
![](https://reactivex.io/documentation/operators/images/S.BehaviorSubject.e.png)

### 3. PublishSubject
- PublishSubject chỉ emit các items được emit sau thời điểm nó được subcribe.
- Lưu ý vì PulishSubject có thể emit item ngay lập tức khi được tạo ra (trừ khi đã có bước ngăn chặn) nên có rủi ro bị mất item trong thời gian Subject được tạo và Observer subcribe. Có thể tránh việc này bằng cách sử dụng operator Create() hoặc đổi sang dùng ReplaySubject.
![](https://reactivex.io/documentation/operators/images/S.PublishSubject.png)
- Nếu có lỗi, BehaviourSubject sẽ không emit gì mà pass thông báo lỗi từ source Observable.
![](https://reactivex.io/documentation/operators/images/S.ReplaySubject.png)

### 4. ReplaySubject
- ReplaySubject emit cho Observer mọi item đã từng được emit, không quan trọng thời gian subcribe.
![](https://reactivex.io/documentation/operators/images/S.ReplaySubject.png)
- Nếu sử dụng ReplaySubject như 1 Observer, cần cẩn thận không gọi onNext() (hay các method on.. khác) trên multi thread bởi điều này có thể dẫ tới coincident calls => vi phạm Observable contract và gây ambiguity bởi không thể xác định item nào nên được replay trước.

## VI. Scheduler
> Để sử dụng multithread với các operators khi các Observers subcribe tới Observable, RX cung cấp 2 method SubcribeOn() và ObserveOn()

- `SubcribeOn(<thread>)`: Ở mặc định, Observable và tập hợp các operators sẽ chạy trên thread mà Subcribe() được gọi. Để thay đổi thread mặc định, ta sử dụng SubcribeOn() ở trong chuối cáci operators để thay đổi thread mà Observable thuộc về.
- `ObserveOn(<thread>)`: Method này sẽ áp dụng các thread cụ thể cho những operators được gọi ở dưới nó. Các operator nằm dưới ObserveOn() sẽ chạy trên thread mà nó gọi tới.
![](https://reactivex.io/documentation/operators/images/schedulers.png)

## VII. RxJava
	RxJava: là RX sử dụng ngôn ngữ Java.
	
>RxJava cũng có những thành phần chính giống như Rx bao gồm Observable, Observer, Schedulers, Operators, Subcription...

### Các thành phần chính
1. Observable: Nguồn phát dữ liệu theo thời gian.
2. Observer: Đối tượng nhận và xử lý các giá trị phát ra từ Observble.
3. Operators: Phép biến đổi và xử lý dữ liệu.
4. Scheduler: Quy định luồng cho các tác vụ.
5. Disposable: Đăng ký và huỷ đăng ký. Cho phép quản lý vòng đời quá trình subcribe và giải phỏng tài nguyên khi không dùng đến.
6. Subject: Observable + Observer. Có thể dùng để tương tác giữa Observable và Observer. 

### Observable và Observer trong RxJava
>Có 5 loại Observable và Observer tương ứng:

Observable | Observer | # of emissions
---------- | -------- | --------------
Observable | Observer | Multiple or None
Single | SingleObserver | 1
Maybe | MabyeObserver | 1 or None
Flowable | Observer | Multiple or none
Completable | CompletableObserver | None (only Complete() or Error())

## VIII. RxAndroid
	RxAndroid là phiên bản Rx đặc biệt được phát triển riêng cho nền tảng Android dựa trên RxJava.
	Do đó, các thành phần trong RxJava đều đúng với trong RxAndroid. Ngoài ra thì trong RxAndroid,
	Schedulers sẽ được hỗ trợ thêm cho multithread giúp phân chia thread cho từng module phù hợp, 
	dễ dàng hơn.
- Schedulers.io(): Dành cho các tác vụ không tốn nhiều CPU như gọi mạng, đọc đĩa/ tệp, CRUD với data...
- AndroidSchedulers.mainThread(): Cho phép xử lý trên Main Thread/ UI thread.
- Schedulers.newThread(): Tạo 1 thread mới cho tác vụ cụ thể.