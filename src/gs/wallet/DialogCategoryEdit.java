package gs.wallet;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class DialogCategoryEdit extends DialogFragment {
	
	/* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the View in case the host needs to query it. 
     */
	public interface DialogEditListener {		
		public void clickDlgDel(View v);
		public void clickDlgChange(View v);
  }

	EditText etCategory;
	String category;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View v = inflater.inflate(R.layout.dlg_category_edit, null);

		builder.setView(v);

		etCategory = (EditText) v.findViewById(R.id.etCategory);
		etCategory.setText(category);
		etCategory.setSelection(category.length());

		return builder.create();
	}

	public void setCategoryEdit(String category) {
		this.category = category;
		return;
	}
	
	public String getCategoryEdit() {
		category = etCategory.getText().toString();
		return category;
	}

}
