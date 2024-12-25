
#  AndroidTest: List and Detail Screens with MVVM

## 1. Cấu trúc Dự Án

Dự án này được phát triển bằng **Android Native** (Java hoặc Kotlin), với mô hình **MVVM** (Model-View-ViewModel) để quản lý logic và dữ liệu dễ dàng.

### Các thành phần chính:
- **Model**: Quản lý dữ liệu (SampleData, Repository).
- **View**: Hiển thị giao diện người dùng (UI).
- **ViewModel**: Quản lý dữ liệu và logic giữa Model và View.

### Tạo hai màn hình chính:
- **Màn hình danh sách (ListFragment)**: Hiển thị danh sách các mục và các tùy chọn sắp xếp.
- **Màn hình chi tiết (DetailFragment)**: Hiển thị thông tin chi tiết của mỗi mục và cho phép xóa mục.

## 2. Quy trình thực hiện

### Bước 1: Chuẩn bị dữ liệu
- **File `sample_data_list.json`** chứa dữ liệu mẫu:

```json
[
  {
    "index": 44,
    "title": "Ethics in Technology",
    "date": "2023-07-20",
    "description": "Explore the ethical considerations in technology and their implications."
  },
  {
    "index": 29,
    "title": "Content Management Systems Explained",
    "date": "2022-04-25",
    "description": "An overview of popular content management systems and their use cases."
  },
  {
    "index": 38,
    "title": "Understanding Cryptocurrency",
    "date": "2023-01-22",
    "description": "Learn the basics of cryptocurrency and how it works."
  },
  ...
]
