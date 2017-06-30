package com.delsk.beautywelfare;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Delsk on 2017/6/30
 */

public class LayoutFromView extends LinearLayout {

    private Context mContext;
    private TextView leftTV;
    private EditText rightEdit;
    private TextView rightTV;
    private View itemView;
    private Type type = Type.EDIT;

    public LayoutFromView(Context context) {
        this(context, null);
    }

    public LayoutFromView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        View rootView = LayoutInflater.from(context).inflate(R.layout.form_item_layout, this);
        this.leftTV = (TextView) rootView.findViewById(R.id.txt);
        this.rightEdit = (EditText) rootView.findViewById(R.id.edit);
        this.rightTV = (TextView) rootView.findViewById(R.id.select_tv);
        this.itemView = rootView.findViewById(R.id.item);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LayoutFromView);
        if (a.hasValue(R.styleable.LayoutFromView_editHint)) {
            rightEdit.setHint(a.getString(R.styleable.LayoutFromView_editHint));
        }
        if (a.hasValue(R.styleable.LayoutFromView_editHintColor)) {
            rightEdit.setHintTextColor(a.getColor(R.styleable.LayoutFromView_editHintColor,
                    ContextCompat.getColor(context, R.color.colorPrimary)));
        }
        if (a.hasValue(R.styleable.LayoutFromView_editText)) {
            rightEdit.setText(a.getString(R.styleable.LayoutFromView_editText));
        }
        if (a.hasValue(R.styleable.LayoutFromView_editTextColor)) {
            rightEdit.setTextColor(a.getColor(R.styleable.LayoutFromView_editTextColor,
                    ContextCompat.getColor(context, R.color.colorPrimary)));
        }
        if (a.hasValue(R.styleable.LayoutFromView_editTextSize)) {
            rightEdit.setTextSize(a.getDimensionPixelSize(R.styleable.LayoutFromView_editTextSize,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14,
                            getResources().getDisplayMetrics())));
        }
        if (a.hasValue(R.styleable.LayoutFromView_leftText)) {
            leftTV.setText(a.getString(R.styleable.LayoutFromView_leftText));
        }
        if (a.hasValue(R.styleable.LayoutFromView_leftTextColor)) {
            leftTV.setTextColor(a.getColor(R.styleable.LayoutFromView_leftTextColor,
                    ContextCompat.getColor(context, R.color.colorPrimary)));
        }
        if (a.hasValue(R.styleable.LayoutFromView_leftTextSize)) {
            leftTV.setTextSize(a.getDimensionPixelSize(R.styleable.LayoutFromView_leftTextSize,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14,
                            getResources().getDisplayMetrics())));
        }
        if (a.hasValue(R.styleable.LayoutFromView_rightText)) {
            rightTV.setText(a.getString(R.styleable.LayoutFromView_rightText));
        }
        if (a.hasValue(R.styleable.LayoutFromView_rightTextColor)) {
            rightTV.setTextColor(a.getColor(R.styleable.LayoutFromView_rightTextColor,
                    ContextCompat.getColor(context, R.color.colorPrimary)));
        }
        if (a.hasValue(R.styleable.LayoutFromView_rightTextSize)) {
            rightTV.setTextSize(a.getDimensionPixelSize(R.styleable.LayoutFromView_rightTextSize,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14,
                            getResources().getDisplayMetrics())));
        }
        if (a.hasValue(R.styleable.LayoutFromView_rightArrow)) {
            Drawable drawable = ContextCompat.getDrawable(context,
                    a.getResourceId(R.styleable.LayoutFromView_rightArrow, R.drawable.delsk_arrow_right));
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            rightTV.setCompoundDrawables(null, null, drawable, null);
        }
        if (a.hasValue(R.styleable.LayoutFromView_itemColor)) {
            itemView.setBackgroundColor(a.getColor(R.styleable.LayoutFromView_itemColor,
                    ContextCompat.getColor(context, R.color.colorPrimary)));
        }
        if (a.hasValue(R.styleable.LayoutFromView_viewType)) {
            int type_id = a.getInt(R.styleable.LayoutFromView_viewType, 0);
            type = Type.values()[type_id];
            setType(type);
        }
        a.recycle();
    }

    /**
     * 动态设置显示类型
     *
     * @param type editview / textview
     */
    public void setType(Type type) {
        if (type == Type.TEXT) {
            rightEdit.setVisibility(GONE);
            rightTV.setVisibility(VISIBLE);
        } else if (type == Type.EDIT) {
            rightEdit.setVisibility(VISIBLE);
            rightTV.setVisibility(GONE);
        } else if (type == Type.NONE) {
            rightEdit.setVisibility(GONE);
            rightTV.setVisibility(VISIBLE);
            rightTV.setText("");
        }
    }

    public void setLeftTV(String str) {
        leftTV.setText(str);
    }

    public String getEditStr() {
        return rightEdit.getText().toString();
    }

    public String getTextStr() {
        return rightEdit.getText().toString();
    }

    public void setItemViewColor(int color) {
        itemView.setBackgroundColor(ContextCompat.getColor(mContext, color));
    }


    public enum Type {EDIT, TEXT, NONE}
}
