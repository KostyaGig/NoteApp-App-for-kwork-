package com.admin.notes.interfaces;

import com.admin.notes.models.Note;

public interface AdapterLIstener {

    void setItemClickListener(int position,int viewId,Note note);
}
