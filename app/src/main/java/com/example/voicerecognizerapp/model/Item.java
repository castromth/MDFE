package com.example.voicerecognizerapp.model;

public class Item {

    private String link;
    private String descricao;
    private String photoLink;


    public String getDescricao() {
        return descricao;
    }

    public String getLink() {
        return link;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setPhotoLink(String photoLink) {
        this.photoLink = photoLink;
    }
}
