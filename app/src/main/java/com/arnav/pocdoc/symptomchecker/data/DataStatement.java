package com.arnav.pocdoc.symptomchecker.data;

public class DataStatement {
    public int selectedItem;
    private String title;

    public DataStatement(String title, int selectedItem) {
        this.title = title;
        this.selectedItem = selectedItem;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
    }
}