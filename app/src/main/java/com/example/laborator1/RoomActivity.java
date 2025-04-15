package com.example.laborator1;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class RoomActivity extends AppCompatActivity {

    EditText titleInput, authorInput, pagesInput, searchTitleInput, minPagesInput, maxPagesInput, filterValueInput, prefixInput;
    ListView listView;
    ArrayAdapter<String> adapter;
    BookDao bookDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        AppDatabase db = AppDatabase.getInstance(this);
        bookDao = db.bookDao();

        titleInput = findViewById(R.id.titleInput);
        authorInput = findViewById(R.id.authorInput);
        pagesInput = findViewById(R.id.pagesInput);
        searchTitleInput = findViewById(R.id.searchTitleInput);
        minPagesInput = findViewById(R.id.minPagesInput);
        maxPagesInput = findViewById(R.id.maxPagesInput);
        filterValueInput = findViewById(R.id.filterValueInput);
        prefixInput = findViewById(R.id.prefixInput);
        listView = findViewById(R.id.listView);

        updateList(bookDao.getAll());

        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> {
            finish();
        });
    }

    public void insertBook(View view) {
        String title = titleInput.getText().toString();
        String author = authorInput.getText().toString();
        int pages = Integer.parseInt(pagesInput.getText().toString());
        bookDao.insert(new Book(title, author, pages));
        updateList(bookDao.getAll());
    }

    public void getAllBooks(View view) {
        updateList(bookDao.getAll());
    }

    public void getByTitle(View view) {
        String title = searchTitleInput.getText().toString();
        Book book = bookDao.getByTitle(title);
        if (book != null) {
            updateList(List.of(book));
        } else {
            Toast.makeText(this, "Cartea nu a fost gasita", Toast.LENGTH_SHORT).show();
        }
    }

    public void getByPageRange(View view) {
        String minText = minPagesInput.getText().toString();
        String maxText = maxPagesInput.getText().toString();

        if (minText.isEmpty() || maxText.isEmpty()) {
            Toast.makeText(this, "Introdu un interval valid de pagini.", Toast.LENGTH_SHORT).show();
            return;
        }

        int min = Integer.parseInt(minText);
        int max = Integer.parseInt(maxText);
        updateList(bookDao.getByPageRange(min, max));
    }

    public void deleteGreater(View view) {
        int value = Integer.parseInt(filterValueInput.getText().toString());
        bookDao.deletePagesGreaterThan(value);
        updateList(bookDao.getAll());
    }

    public void deleteLess(View view) {
        int value = Integer.parseInt(filterValueInput.getText().toString());
        bookDao.deletePagesLessThan(value);
        updateList(bookDao.getAll());
    }

    public void incrementByPrefix(View view) {
        String prefix = prefixInput.getText().toString();
        bookDao.incrementPagesForTitlePrefix(prefix);
        updateList(bookDao.getAll());
    }

    private void updateList(List<Book> books) {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        for (Book book : books) {
            adapter.add(book.id + ": " + book.title + " - " + book.author + " (" + book.pages + " pagini)");
        }
        listView.setAdapter(adapter);
    }
}
