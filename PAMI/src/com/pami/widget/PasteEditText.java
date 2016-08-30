package com.pami.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;

public class PasteEditText extends EditText{
    private Context context;
    
    
    public PasteEditText(Context context) {
        super(context);
        this.context = context;
    }

    public PasteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public PasteEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }
    
    @SuppressLint("NewApi")
	@Override
    public boolean onTextContextMenuItem(int id) {
        if(id == android.R.id.paste){
            ClipboardManager clip = (ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            String text = clip.getText().toString();
            if(text.startsWith("EASEMOBIMG")){
                text = text.replace("EASEMOBIMG", "");
                Intent intent = new Intent(context,AlertDialog.class);
                intent.putExtra("title", "发送以下图片？");
                intent.putExtra("forwardImage", text);
                intent.putExtra("cancel", true);
                ((Activity)context).startActivityForResult(intent,11);
            }
        }
        return super.onTextContextMenuItem(id);
    }
    
    
    
    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if(!TextUtils.isEmpty(text) && text.toString().startsWith("EASEMOBIMG")){
            setText("");
        }
//        else if(!TextUtils.isEmpty(text)){
//        	setText(SmileUtils.getSmiledText(getContext(), text),BufferType.SPANNABLE);
//        }
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
    }
    

}
