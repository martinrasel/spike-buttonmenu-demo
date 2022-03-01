package de.bembelnaut.spike.buttonmenudemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupMenu;
import android.widget.Toast;

import de.bembelnaut.menudemo.R;

public class MainActivity extends AppCompatActivity {

    private ActionMode actionMode;

    /*
     * Method is called by app:method on button
     */
    public void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.popup, popupMenu.getMenu());
        popupMenu.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View first = findViewById(R.id.mybutton);
        // CONTEXT-MENU: Will call the onCreateContextMenu() with this view/button as parameter
        registerForContextMenu(first);

        View second = findViewById(R.id.mybutton2);
        // CALL-ACTION-BAR-MENU: Register a *long* click listener
        second.setOnLongClickListener(new View.OnLongClickListener() {
            // Called when the user long-clicks on someView
            public boolean onLongClick(View view) {
                if (actionMode != null) {
                    return false;
                }

                // CALL-ACTION-BAR-MENU: Start the CAB using the ActionMode.Callback defined above
                actionMode = MainActivity.this.startActionMode(actionModeCallback);
                view.setSelected(true);
                return true;
            }
        });
    }

    /*
     * MENU-BAR: Method is called after onCreate() by main frame.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*
     * MENU-BAR: Method by options of the bar
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        Toast.makeText(this, "Option Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        return true;
    }

    /*
     * CONTEXT-MENU:Called by method registerForContextMenu() in method onCreate.
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        // add programical created menu item
        menu.add(R.id.mygroup, R.id.myMenuItemId, 0, "Yellow");

        // Create a inflater to create view element and assign it on the given menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.second_menu, menu);
    }

    /*
     * CONTEXT-MENU: Called by method registerForContextMenu() in method onCreate.
     */
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.bar:
            case R.id.dou:

                Toast.makeText(this, "Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.myMenuItemId:
                Toast.makeText(this, "MyItem: " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    /*
     * CALL-ACTION-BAR-MENU: implementation of CAB
     */
    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.second_menu, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.bar:
                case R.id.dou:
                    Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                    mode.finish(); // !!!!!!!!!!!!!!! Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
        }
    };
}