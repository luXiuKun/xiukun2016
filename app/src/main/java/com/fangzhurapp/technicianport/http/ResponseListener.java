/*
 * Copyright © YOLANDA. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fangzhurapp.technicianport.http;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.fangzhurapp.technicianport.CustomApplication;
import com.yolanda.nohttp.error.NetworkError;
import com.yolanda.nohttp.error.NotFoundCacheError;
import com.yolanda.nohttp.error.ServerError;
import com.yolanda.nohttp.error.TimeoutError;
import com.yolanda.nohttp.error.URLError;
import com.yolanda.nohttp.error.UnKnownHostError;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import com.fangzhurapp.technicianport.view.WaitDialog;

/**
 * Created in Mar 6, 2016 9:01:42 PM.
 * 
 * @author YOLANDA;
 */
public class ResponseListener<T> implements OnResponseListener<T> {

	private Request<T> mRequest;

	private WaitDialog mDialog;

	private HttpCallBack<T> callBack;

	private boolean isShowError;

	public ResponseListener(Request<T> request, Context context, HttpCallBack<T> callBack, boolean isShowDialog, boolean isCanCancel, boolean isShowError) {
		this.mRequest = request;
		this.callBack = callBack;
		this.isShowError = isShowError;
		if (context != null && isShowDialog) {
			mDialog = new WaitDialog(context);
			mDialog.setCancelable(isCanCancel);
			mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					//mRequest.cancel(true);
					mRequest.cancel();
				}
			});
		}
	}

	@Override
	public void onStart(int what) {
		if (mDialog != null && !mDialog.isShowing())
			mDialog.show();
	}

	@Override
	public void onSucceed(int what, Response<T> response) {
		if (callBack != null)
			callBack.onSucceed(what, response);
	}

	@Override
	public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
		if (isShowError) {
			/*if (exception instanceof ClientError) {// 客户端错误
				Toast.makeText(MyApplication.getInstance(), "客户端发生错误", Toast.LENGTH_SHORT).show();
			} else*/
			if (exception instanceof ServerError) {// 服务器错误
				Toast.makeText(CustomApplication.getInstance(), "服务器发生错误", Toast.LENGTH_SHORT).show();
			} else if (exception instanceof NetworkError) {// 网络不好
				Toast.makeText(CustomApplication.getInstance(), "请检查网络", Toast.LENGTH_SHORT).show();
			} else if (exception instanceof TimeoutError) {// 请求超时
				Toast.makeText(CustomApplication.getInstance(), "请求超时，网络不好或者服务器不稳定", Toast.LENGTH_SHORT).show();
			} else if (exception instanceof UnKnownHostError) {// 找不到服务器
				Toast.makeText(CustomApplication.getInstance(), "未发现指定服务器", Toast.LENGTH_SHORT).show();
			} else if (exception instanceof URLError) {// URL是错的
				Toast.makeText(CustomApplication.getInstance(), "URL错误", Toast.LENGTH_SHORT).show();
			} else if (exception instanceof NotFoundCacheError) {
				Toast.makeText(CustomApplication.getInstance(), "没有发现缓存", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(CustomApplication.getInstance(), "未知错误", Toast.LENGTH_SHORT).show();
			}
		}
		if (callBack != null)
			callBack.onFailed(what, url, tag, exception, responseCode, networkMillis);
	}

	@Override
	public void onFinish(int what) {
		if (mDialog != null && mDialog.isShowing())
			mDialog.dismiss();
	}

}
