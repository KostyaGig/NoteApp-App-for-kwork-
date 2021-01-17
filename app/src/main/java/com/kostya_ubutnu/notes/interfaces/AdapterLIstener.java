package com.kostya_ubutnu.notes.interfaces;

import com.kostya_ubutnu.notes.models.Note;

public interface AdapterLIstener {

    void setItemClickListener(int position,int viewId,Note note);
}
