
package net.cattaka.android.gcmadkalart;

import net.cattaka.android.gcmadkalart.db.DbHelper;
import net.cattaka.android.gcmadkalart.util.MyPreference;
import net.cattaka.libgeppa.IPassiveGeppaService;

public interface IAppStub {
    public IPassiveGeppaService getGeppaService();

    public DbHelper getDroiballDatabase();

    public MyPreference getMyPreference();
}
