package fr.univ_tln.trailscommunity.utilities.fonts;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import fr.univ_tln.trailscommunity.R;

/**
 * Created by Stéphen on 03/12/2016.
 */

public class CustomTextView extends TextView {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStylesRes){
        super(context, attrs, defStyleAttr, defStylesRes);
        init(attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        init(attrs);
    }

    public CustomTextView(Context context){
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs){
        if(attrs != null){
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTextView);
            String fontName = a.getString((R.styleable.CustomTextView_font));

            try{
                if(fontName != null){
                    Typeface myTypeFace = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + fontName);
                    setTypeface(myTypeFace);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            a.recycle();
        }
    }

}

