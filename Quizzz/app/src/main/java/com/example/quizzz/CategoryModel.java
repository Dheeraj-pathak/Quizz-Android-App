package com.example.quizzz;

public class CategoryModel {
    private String categoryId, categoryName, categoryImage;

    public CategoryModel(String categoryId, String categoryName, String categoryImage) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
    }
    public CategoryModel() {
        // Required empty constructor for Firestore deserialization
    }

//    public void  CategoryModel(String s, String s1, String s2){
//    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }
}
