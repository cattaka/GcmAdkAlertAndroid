
package net.cattaka.android.gcmadkalert;

import net.cattaka.android.gcmadkalert.util.MyPreference;
import net.cattaka.libgeppa.IPassiveGeppaService;

public interface IAppStub {
    public IPassiveGeppaService getGeppaService();

    public MyPreference getMyPreference();
}
