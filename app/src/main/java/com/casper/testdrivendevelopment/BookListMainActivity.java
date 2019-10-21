package com.casper.testdrivendevelopment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.casper.testdrivendevelopment.data.BookSaver;
import com.casper.testdrivendevelopment.data.model.Book;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class BookListMainActivity extends AppCompatActivity {

    public static final int CONTEXT_MENU_DELETE=1;
    public static final int CONTEXT_MENU_NEW=CONTEXT_MENU_DELETE+1;
    public static final int CONTEXT_MENU_UPDATE = CONTEXT_MENU_NEW+1;
    public static final int CONTEXT_MENU_ABOUT=CONTEXT_MENU_UPDATE +1;
    public static final int REQUEST_CODE_NEW_BOOK=901;
    public static final int REQUEST_CODE_UPDATE_BOOK=902;

    //@Override
    private ListView listViewBooks;
    private ArrayList<Book> listBooks= new ArrayList<>();
   private BookAdapter bookAdapter;
   private BookSaver bookSaver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_main);

        bookSaver=new BookSaver(this);
        listBooks=bookSaver.load();
        if(listBooks.size()==0)
             init();
        listViewBooks=(ListView)this.findViewById(R.id.list_view_books);
        bookAdapter = new BookAdapter(BookListMainActivity.this, R.layout.list_view_item_book, listBooks);
        listViewBooks.setAdapter(bookAdapter);

        this.registerForContextMenu(listViewBooks);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
       bookSaver.save();
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

         super.onCreateContextMenu(menu, v, menuInfo);
        if(v==listViewBooks){
            // 获取适配器
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            //设置标题
            menu.setHeaderTitle(listBooks.get(info.position).getTitle());
            //添加内容
            menu.add(0, CONTEXT_MENU_DELETE, 0, "删除");
            menu.add(0, CONTEXT_MENU_NEW, 0, "新建");
            menu.add(0, CONTEXT_MENU_UPDATE, 0, "更新");
            menu.add(0, CONTEXT_MENU_ABOUT, 0, "关于...");

        }
    }
    //onActivityResult（）是从NewBookActiviy回调到BookListActivity的
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {
            case REQUEST_CODE_NEW_BOOK:
                if(resultCode==RESULT_OK)
                {
                   int position=data.getIntExtra("edit_position",0);
                    String title=data.getStringExtra("book_title");
                    double price =data.getDoubleExtra("book_price",0);
                    getListBooks().add(position, new Book(title,R.drawable.book_no_name,price));
                    //listBooks.add(position, new Book(title,R.drawable.book_no_name,price));
                    //通知适配器已改变
                    bookAdapter.notifyDataSetChanged();

                    Toast.makeText(this, "新建成功", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CODE_UPDATE_BOOK:
                if(resultCode==RESULT_OK)
                {
                    int position=data.getIntExtra("edit_position",0);
                    String name=data.getStringExtra("book_title");
                    double price=data.getDoubleExtra("book_price",0);
                    Book book= getListBooks().get(position);
                   // Book book=listBooks.get(position);
                    book.setTitle(name);
                    book.setPrice(price);//book类没有price
                    //通知适配器已改变
                    bookAdapter.notifyDataSetChanged();

                    Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case CONTEXT_MENU_NEW: {
                AdapterView.AdapterContextMenuInfo menuInfo=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

                Intent intent = new Intent(BookListMainActivity.this,NewBookActivity.class);
                intent.putExtra("edit_position",menuInfo.position);
                intent.putExtra("book_title","无名书籍");
                intent.putExtra("book_price","0.0");

                //startActivityForResult()方法是主活动BookListActivity用来启动NewBookActivity的
                startActivityForResult(intent,REQUEST_CODE_NEW_BOOK );
                break;
            }
            case CONTEXT_MENU_UPDATE: {
                AdapterView.AdapterContextMenuInfo menuInfo=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                Book book=listBooks.get(menuInfo.position);

                Intent intent = new Intent(BookListMainActivity.this,NewBookActivity.class);
                intent.putExtra("edit_position",menuInfo.position);
                intent.putExtra("book_title",book.getTitle());
                intent.putExtra("book_price",book.getPrice());
                //startActivityForResult()方法是主活动BookListActivity用来启动NewBookActivity的
                startActivityForResult(intent, REQUEST_CODE_UPDATE_BOOK);
                break;
            }
            case CONTEXT_MENU_DELETE: {
                AdapterView.AdapterContextMenuInfo menuInfo=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                final int itemPosition=menuInfo.position;
                new android.app.AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("询问")
                        .setMessage("你确定要删除这条吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                listBooks.remove(itemPosition);
                                bookAdapter.notifyDataSetChanged();
                                Toast.makeText(BookListMainActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create().show();
                break;
            }
            case CONTEXT_MENU_ABOUT:
                Toast.makeText(this, "版权所有by shpchen!", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    public List<Book> getListBooks()
    {
        return listBooks;
    }

    private void init(){

        listBooks.add(new Book("软件项目管理案例教程（第4版）", R.drawable.book_2,1.0));
        listBooks.add(new Book("创新工程实践", R.drawable.book_no_name,2.0));
        listBooks.add(new Book("信息安全数学基础（第2版）", R.drawable.book_1,3.0));

    }


    class BookAdapter extends ArrayAdapter<Book> {

        private int resourceId;

     public BookAdapter(Context context, int resource, List<Book> objects) {
          super(context, resource, objects);
            resourceId = resource;
       }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            /*Book book = getItem(position);//获取当前项的实例
            View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            ((ImageView) view.findViewById(R.id.image_view_book_cover)).setImageResource(book.getCoverResourceId());
            ((TextView) view.findViewById(R.id.text_view_book_title)).setText(book.getTitle());
            ((TextView)view.findViewById(R.id.textView_book_price)).setText("价格："+book.getPrice()+"元");

            return view;*/
           //getview函数必须重写，否则无法进入App
           LayoutInflater mInflater= LayoutInflater.from(this.getContext());
            View item = mInflater.inflate(this.resourceId,null);

            ImageView bookImage = item.findViewById(R.id.image_view_book_cover);
            TextView bookTitle = item.findViewById(R.id.text_view_book_title);

            Book book_item = this.getItem(position);
            bookImage.setImageResource(book_item.getCoverResourceId());
            bookTitle.setText(book_item.getTitle()+ "," + book_item.getPrice() + "元");

            return item;
        }
    }
}

