
package net.cattaka.android.gcmadkalert.fragment;

import net.cattaka.android.gcmadkalert.IAppListener;
import net.cattaka.android.gcmadkalert.IAppStub;
import net.cattaka.android.gcmadkalert.util.MyPreference;
import net.cattaka.libgeppa.IPassiveGeppaService;
import android.support.v4.app.Fragment;

public abstract class BaseFragment extends Fragment implements IAppListener {
    protected IPassiveGeppaService getGeppaService() {
        return ((IAppStub)getActivity()).getGeppaService();
    }

    protected IAppStub getAppStub() {
        return (IAppStub)getActivity();
    }

    public MyPreference getMyPreference() {
        return getAppStub().getMyPreference();
    }
}
