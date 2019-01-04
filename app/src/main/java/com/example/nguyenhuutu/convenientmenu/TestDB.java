//package com.example.nguyenhuutu.convenientmenu;
//
//import android.support.annotation.NonNull;
//import android.widget.Button;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class TestDB {
//    /**
//     * Các thao tác với database
//     */
//
//    /**
//     * Thêm Document (hay còn gọi là bộ trong SQL) vào Collection (còn gọi là Table trong SQL)
//     * Dùng các cách sau: add(), set()
//     */
//
//
//    // thao tác thêm data
//
//    /**
//     * Chú ý: trong database sẽ có collection tên "Info" lưu thông tin là id lớn nhất hiện tại đc cấp của từng table.
//     * Mọi người lên console firebase xem nha. Mình sẽ gửi tài khoản
//     * - Khi thêm mới dữ liệu vào collection thì mọi người phải lên collection "Info" lấy id lớn nhất, sau đó:
//     *  + tăng lên 1 đơn vị
//     *  + dùng hàm tạo id do class của từng table cung cấp để thêm dữ liệu
//     *  + update lại id này lên collection "Info"
//     */
//    void processAdd () {
//
//
//        CMDB.db.collection("Info").document("dish_id")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Integer maxId = ((Number)document.get("dish_id_max")).intValue();
//                        maxId++;
//                        String dishId = Dish.createDishId(maxId);
//
//                        List<String> imgs = new ArrayList<>();
//                        List<String> addresses = new ArrayList<>();
//                        imgs.add("file01.jpg");
//                        imgs.add("file02.jpg");
//                        imgs.add("file03.jpg");
//
//                        addresses.add("address 01");
//                        addresses.add("address 02");
//                        addresses.add("address 03");
//
//                        String dishName = "dish 01";
//                        Integer dishprice = 30000;
//                        String dishDescription = "dish 01 dish 01";
//                        String dishHomeImage = "file0.jpg";
//                        List<String> dishMoreImages = imgs;
//                        String menuId = "MENU001";
//                        String dishTypeId = "DTYPE01";
//
//                        /**
//                         * Dùng set()
//                         */
//                        Map<String, Object> data = Dish.createDishData(dishId, dishName, dishprice, dishDescription, dishHomeImage, dishMoreImages, menuId, dishTypeId);
//                        CMDB.db.collection("Dish").document("")
//                                .set(data)
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
////                                    public void onSuccess(Void aVoid) {
////                                        // code here
////                                    }
////                                })
////                                .addOnFailureListener(new OnFailureListener() {
////                                    @Override
////                                    public void onFailure(@NonNull Exception e) {
////                                        // code here
////                                    }
////                                });
//
//                        /**
//                         * Dùng add()
//                         */
//                        CMDB.db.collection("Dish").add(data)
//                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                    @Override
//                                    public void onSuccess(DocumentReference documentReference) {
//                                        /**
//                                         * documentReference.getId() trả về id của document mình vừa thêm vào
//                                         */
//                                        // code here
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        // code here
//                                    }
//                                });
//
//                        // Cập nhật lại id
//                        Map<String, Object> dataUpdate = new HashMap<>();
//                        data.put("dish_id_max", maxId);
//                        CMDB.db.collection("Info").document("dish_id").set(dataUpdate);
//                    } else {
//
//                    }
//                } else {
//
//                }
//            }
//        });
//
//        /**
//         * Giải thích:
//         * - collection(<tên collection (tên table)>)
//         * - document(<id của document (dòng dữ liệu trong collection)>)
//         * - trong database thì mỗi document trong từng collection sẽ đc hệ thống tự động tạo ra một id của riêng document
//         *  + khi dùng add() để thêm document thì id của document sẽ đc tạo tự động bởi hệ thống => id rất dài ko kiếm soát đc
//         *  + Khi dùng document(id).set() để thêm document thì lúc này mình có bảo với hệ thống rằng sẽ dùng id mình cung cấp
//         *  để thay cho id mà hệ thống cung cấp nên lúc này mình kiểm soát đc id của document.
//         * ==> tuy nhiên trong đồ án này sẽ dùng set() nha, những table nào mà có nhiều field làm khóa chính thì id đc tạo ra chỉ để nhàm mục đích thuận tiện cho việc xóa và update
//         */
//        /**
//         * Những collection còn lại làm tương tự. Các hàm hỗ trợ sẳn sẽ đc liệt kê trong sheet gửi mọi người
//         */
//    }
//
//    void processUpdate() {
//        /**
//         * làm tương tự phần processAdd() những khác chỗ ko tạo id mới. Khi set() mà trùng id đã có thì nó sẽ bỏ cái document cũ thay bằng document mới
//         */
//    }
//
//    void processDelete() {
//        CMDB.db.collection("Dish").document("DISH0001").delete()
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        // code here
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        // code here
//                    }
//                });
//
//        /**
//         * document() truyền vào id của document mình cần xóa
//         * Khi lấy dữ liệu về thì sẽ có một hàm lấy Id của document mình đang xét
//         */
//    }
//
//    void processGet() {
//        /**
//         * lấy dữ liệu của một document
//         */
//        CMDB.db.collection("Dish").document("DISH0001")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot document = task.getResult();
//                            if (document.exists()) {
//                                /**
//                                 * document.getId() trả về id của document mình vừa lấy (="DISH0001")
//                                 */
//                                //code here
//                                Dish dish = Dish.loadDish(document.getData());
//                                /**
//                                 * Dùng các phương thức getter của dish để lấy dữ liệu
//                                 * các class khác có cách dùng tương tự (sẽ có hàm tưng ứng trong từng class)l
//                                 */
//                            } else {
//                                // code here
//                            }
//                        } else {
//                            // code here
//                        }
//                    }
//                });
//
//        /**
//         * Lấy nhiều document
//         */
//        CMDB.db.collection("Dish").get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                // code here
//                            }
//                        } else {
//                            // code here
//                        }
//                    }
//                });
//        /**
//         * lấy documents từ nhiều điều kiện custom
//         */
//        CMDB.db.collection("Dish")
//                .whereEqualTo("dish_id", "DISH0001")
//                .whereGreaterThan("dish_price", 45000)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                // code here
//                            }
//                        } else {
//                            // code here
//                        }
//                    }
//                });
//        /**
//         * Lưu ý: Các điều kiện tiêu biểu sau:
//         * - whereEqualTo()
//         * - whereGreaterThan()
//         * - whereGreaterThanOrEqualTo()
//         * - whereLessThan()
//         * - whereLessThanOrEqualTo()
//         *
//         * Ngoài ra còn có các điều kiện khác, mọi ng tự tìm hiểu nha :)))
//         */
//        /**
//         * Một chú ý nữa: ở trên biến document có method document.getData() trả về Map<String, Object>. kết quả này đc dùng để convert sang
//         * đối tượng tương ứng để lấy dữ liệu như trên
//         */
//    }
//
//}
