package gs.wallet;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Class with multiple Dialogs:
 * DialogCategory
 * DialogIcons
 * DialogDate
 * 
 * @author AndrewComeloff
 *
 */
public class Dialogs extends DialogFragment {
//	protected Dialogs() {};

	public final static int DIALOG_CATEGORY = 0;
	public final static int DIALOG_ICONS = 1;
	public final static int DIALOG_DATE = 2;
	
	/**
	 * Method return dialog by id
	 * 
	 * @param id of the Dialog 
	 * @param context
	 * @return Dialog, from id
	 */
	public static Dialog getDialogById(int id, final Context context) {
		
		Dialog dialog = null;
		switch (id) {
		case DIALOG_CATEGORY:
			
			dialog = createCategoryDialog(context);
			break;
		case DIALOG_ICONS:
			
			dialog = createIconsDialog(context);
			break;
		case DIALOG_DATE:
			
			dialog = createDateDialog(context);
			
			break;
		}
		return dialog;
	}

	private static Dialog createCategoryDialog(Context context) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private static Dialog createIconsDialog(Context context) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private static Dialog createDateDialog(final Context context) {
		
		Dialog dialog;
		Builder builder = new Builder(context);
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
	    final View v = inflater.inflate(R.layout.dlg_category, null);
		return null;
	}

}
