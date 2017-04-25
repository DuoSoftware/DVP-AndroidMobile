package apps.veery.com.utilities;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Sijith on 11/1/2016.
 */
public class FontChanger {
    public static final String FONT_REGULAR = "regular";
    public static final String FONT_BOLD = "bold";
    public static void changeFont(List<View> viewList, Context context,String typeface){
        Typeface typefaceToSet = null;
        if(typeface.equals(FONT_REGULAR)){
            typefaceToSet = Typeface.createFromAsset(context.getAssets(), "fonts/AvenirNextLTPro-Regular.otf");
        }else if(typeface.equals(FONT_BOLD)){
            typefaceToSet = Typeface.createFromAsset(context.getAssets(), "fonts/AvenirNextLTPro-Bold.otf");
        }else{
            // can use if program needs to give a new typeface
        }

        for (View v:viewList) {
            if(v instanceof TextView){
                ((TextView) v).setTypeface(typefaceToSet);
            }
            else if(v instanceof EditText){
                ((EditText) v).setTypeface(typefaceToSet);
            }
            else{
                // do nothing on unspecified views
            }

        }
    }

}
