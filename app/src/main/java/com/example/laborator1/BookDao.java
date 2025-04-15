package com.example.laborator1;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BookDao {

    @Insert
    void insert(Book book);

    @Query("SELECT * FROM Book")
    List<Book> getAll();

    @Query("SELECT * FROM Book WHERE title = :title")
    Book getByTitle(String title);

    @Query("SELECT * FROM Book WHERE pages BETWEEN :min AND :max")
    List<Book> getByPageRange(int min, int max);

    @Query("DELETE FROM Book WHERE pages > :value")
    void deletePagesGreaterThan(int value);

    @Query("DELETE FROM Book WHERE pages < :value")
    void deletePagesLessThan(int value);

    @Query("UPDATE Book SET pages = pages + 1 WHERE title LIKE :prefix || '%'")
    void incrementPagesForTitlePrefix(String prefix);
}
