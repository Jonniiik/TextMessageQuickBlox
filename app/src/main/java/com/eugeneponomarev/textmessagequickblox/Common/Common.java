package com.eugeneponomarev.textmessagequickblox.Common;

import com.eugeneponomarev.textmessagequickblox.Holder.QBUsersHolder;
import com.quickblox.users.model.QBUser;

import java.util.List;

public class Common {
    public static String createChatDialogName(List<Integer> qbUsers) {
        List<QBUser> qbUserList = QBUsersHolder.getInstance().getUsersByIds(qbUsers);

        StringBuilder name = new StringBuilder();
        for (QBUser user : qbUserList)
            name.append(user.getFullName()).append(" ");
        if (name.length() > 30)
            name = name.replace(30, name.length() - 1, "...");

        return name.toString();
    }
}
