
// =========================
// Calculator.java
// =========================

package com.example.laborator1;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

public class Calculator implements Parcelable, Serializable {
    private String nume;
    private boolean disponibilitate;
    private int rating;
    private Category categorie;
    private boolean status;
    private boolean functional;
    private Date createdAt;

    public Calculator(String nume, boolean disponibilitate, int rating, Category categorie, boolean status, boolean functional, Date createdAt) {
        this.nume = nume;
        this.disponibilitate = disponibilitate;
        this.rating = rating;
        this.categorie = categorie;
        this.status = status;
        this.functional = functional;
        this.createdAt = createdAt;
    }

    protected Calculator(Parcel in) {
        nume = in.readString();
        disponibilitate = in.readByte() != 0;
        rating = in.readInt();
        categorie = Category.valueOf(in.readString());
        status = in.readByte() != 0;
        functional = in.readByte() != 0;
        createdAt = new Date(in.readLong());
    }

    public static final Creator<Calculator> CREATOR = new Creator<Calculator>() {
        @Override
        public Calculator createFromParcel(Parcel in) {
            return new Calculator(in);
        }

        @Override
        public Calculator[] newArray(int size) {
            return new Calculator[size];
        }
    };

    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }
    public boolean isDisponibilitate() { return disponibilitate; }
    public void setDisponibilitate(boolean disponibilitate) { this.disponibilitate = disponibilitate; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public Category getCategorie() { return categorie; }
    public void setCategorie(Category categorie) { this.categorie = categorie; }
    public boolean getStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
    public boolean isFunctional() { return functional; }
    public void setFunctional(boolean functional) { this.functional = functional; }
    public Date getDateTime() { return createdAt; }
    public void setDateTime(Date dateTime) { this.createdAt = dateTime; }

    public enum Category {
        PC,
        LAPTOP,
        OTHER
    }

    @NonNull
    @Override
    public String toString() {
        return "Nume: " + nume +
                "\nDisponibilitate: " + (disponibilitate ? "Da" : "Nu") +
                "\nRating: " + rating +
                "\nCategorie: " + categorie +
                "\nStatus: " + (status ? "Activ" : "Inactiv") +
                "\nFunctional: " + (functional ? "Da" : "Nu") +
                "\nData: " + createdAt.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nume);
        dest.writeByte((byte) (disponibilitate ? 1 : 0));
        dest.writeInt(rating);
        dest.writeString(categorie.name());
        dest.writeByte((byte) (status ? 1 : 0));
        dest.writeByte((byte) (functional ? 1 : 0));
        dest.writeLong(createdAt.getTime());
    }
}
