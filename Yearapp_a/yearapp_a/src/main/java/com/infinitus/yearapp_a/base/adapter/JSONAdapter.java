package com.infinitus.yearapp_a.base.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.infinitus.yearapp_a.base.auth.TokenManager;
import com.infinitus.yearapp_a.base.util.JSONUtils;

public abstract class JSONAdapter extends ArrayAdapter<JSONObject> {
	protected AQuery aq;

	protected boolean pageable = false;
	private int pageActurl = 1; //实际已加载的页数
	private int page = 1;		//将要加载的页数

	private int pageSize = 10;

	private String pageQueryName = "page";
	private String pageQuerySizeName = "pageSize";

	private String url;
	private Map<String, Object> params;
	private boolean _hasMore = false;

	private List<NetworkListener> listeners;

	public JSONAdapter(Context context) {
		this(context, 0);
	}

	public JSONAdapter(Context context, int resource) {
		super(context, resource);

		aq = new AQuery(context);

		url = getNetworkUrl();

		params = getNetworkParams();
	}

	public void setNetworkListener(NetworkListener listener) {
		assert (listener != null);
		if (listeners == null) {
			listeners = new ArrayList<NetworkListener>();
		}
		listeners.add(listener);
	}
	
	public int getPage() {
		return page;
	}
	
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public boolean hasMore() {
		return _hasMore;
	}

	public void load(boolean more) {
		if (url != null)
			network(more);
	}

	public void setPageable(boolean pageable) {
		this.pageable = pageable;
	}

	protected String getNetworkUrl() {
		return null;
	}

	protected Map<String, Object> getNetworkParams() {
		return null;
	}

	protected void handleNetworkResult(Object object, boolean hasMore) {
		JSONArray array = (JSONArray) object;
		if (array != null) {
			for (int i = 0; i < array.length(); i++) {
				JSONObject jo = array.optJSONObject(i);
				add(jo);
			}
		}
	}

	private void network(final boolean more) {
		page = more ? (pageActurl + 1) : 1;

		StringBuffer newUrl = new StringBuffer(url);
		if (pageable) {
			if (url.indexOf("?") == -1) {
				newUrl.append("?");
			} else {
				newUrl.append("&");
			}
			newUrl.append(pageQueryName).append("=").append((page));
			newUrl.append("&").append(pageQuerySizeName).append("=").append((pageSize));

		}

		AjaxCallback<JSONObject> cb = new AjaxCallback<JSONObject>() {
			@Override
			public void callback(String url, JSONObject object,
					AjaxStatus status) {

				String error = JSONUtils.error(object, status);
				if (error == null) {
					setNotifyOnChange(false);

					if (!more) {
						clear();
					}

					JSONAdapter.this.handleNetworkResult(object, more);

					JSONObject data = object.optJSONObject("data");

					if (pageable) {
						pageActurl = page;
						if (data != null && data.optInt("total") > page * pageSize) {
							_hasMore = true;
						} else {
							_hasMore = false;
						}
					}
					
					notifyDataSetChanged();
				}

				postNitification(error);
			}
		};

		aq.auth(TokenManager.instance().getTokenHandle());

		if (params != null) {
				aq.ajax(newUrl.toString(), params, JSONObject.class, cb);
		} else {
			aq.ajax(newUrl.toString(), JSONObject.class, cb);
		}
	}

	private void postNitification(String error) {
		for (Iterator<NetworkListener> iterator = listeners.iterator(); iterator
				.hasNext();) {
			NetworkListener listener = iterator.next();
			listener.finish(error, _hasMore);
		}
	}

	public void setNetworkParams(Map<String, Object> params) {
		this.params = params;
	}

	public void setURL(String url) {
		this.url = url;
	}
}
