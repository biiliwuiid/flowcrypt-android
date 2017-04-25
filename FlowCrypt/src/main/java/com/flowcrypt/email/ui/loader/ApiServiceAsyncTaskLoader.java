package com.flowcrypt.email.ui.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.flowcrypt.email.api.retrofit.ApiHelper;
import com.flowcrypt.email.api.retrofit.ApiService;
import com.flowcrypt.email.api.retrofit.BaseResponse;
import com.flowcrypt.email.api.retrofit.request.BaseRequest;
import com.flowcrypt.email.api.retrofit.request.LookUpEmailRequest;
import com.flowcrypt.email.api.retrofit.request.MessagePrototypeRequest;
import com.flowcrypt.email.api.retrofit.response.LookUpEmailResponse;
import com.flowcrypt.email.api.retrofit.response.MessagePrototypeResponse;

/**
 * A basic AsyncTaskLoader who make API calls.
 *
 * @author Denis Bondarenko
 *         Date: 10.03.2015
 *         Time: 13:42
 *         E-mail: DenBond7@gmail.com
 */
public class ApiServiceAsyncTaskLoader extends AsyncTaskLoader<BaseResponse> {
    private ApiHelper apiHelper;
    private BaseRequest baseRequest;
    private ApiService apiService;

    public ApiServiceAsyncTaskLoader(Context context, BaseRequest baseRequest) {
        super(context);
        this.apiHelper = ApiHelper.getInstance();
        this.baseRequest = baseRequest;
        onContentChanged();
    }

    @Override
    public void onStartLoading() {
        if (takeContentChanged())
            forceLoad();
    }

    @Override
    public BaseResponse loadInBackground() {
        if (apiHelper != null && apiHelper.getRetrofit() != null) {
            apiService = apiHelper.getRetrofit().create(ApiService.class);

            if (baseRequest != null && baseRequest.getApiName() != null) {
                switch (baseRequest.getApiName()) {
                    case POST_LOOKUP_EMAIL:
                        BaseResponse<LookUpEmailResponse> lookUpEmailResponse =
                                new BaseResponse<>();
                        lookUpEmailResponse.setApiName(baseRequest.getApiName());

                        LookUpEmailRequest lookUpEmailRequest = (LookUpEmailRequest) baseRequest;

                        if (apiService != null) {
                            try {
                                lookUpEmailResponse.setResponse(apiService.postLookUpEmail
                                        (lookUpEmailRequest.getPostLookUpEmailModel()).execute());
                            } catch (Exception e) {
                                e.printStackTrace();
                                lookUpEmailResponse.setException(e);
                            }
                        }
                        return lookUpEmailResponse;

                    case POST_MESSAGE_PROTOTYPE:
                        BaseResponse<MessagePrototypeResponse> messagePrototypeResponse =
                                new BaseResponse<>();
                        messagePrototypeResponse.setApiName(baseRequest.getApiName());

                        MessagePrototypeRequest messagePrototypeRequest =
                                (MessagePrototypeRequest) baseRequest;

                        if (apiService != null) {
                            try {
                                messagePrototypeResponse.setResponse(apiService
                                        .postMessagePrototype(messagePrototypeRequest
                                                .getPostMessagePrototypeModel()).execute());
                            } catch (Exception e) {
                                e.printStackTrace();
                                messagePrototypeResponse.setException(e);
                            }
                        }
                        return messagePrototypeResponse;
                }
            }
        }

        return null;
    }

    @Override
    public void onStopLoading() {
        cancelLoad();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiServiceAsyncTaskLoader)) return false;

        ApiServiceAsyncTaskLoader that = (ApiServiceAsyncTaskLoader) o;

        if (apiHelper != null ? !apiHelper.equals(that.apiHelper) : that
                .apiHelper != null)
            return false;
        if (baseRequest != null ? !baseRequest.equals(that.baseRequest) : that.baseRequest != null)
            return false;
        return !(apiService != null ? !apiService.equals(that.apiService) : that.apiService !=
                null);

    }

    @Override
    public int hashCode() {
        int result = apiHelper != null ? apiHelper.hashCode() : 0;
        result = 31 * result + (baseRequest != null ? baseRequest.hashCode() : 0);
        result = 31 * result + (apiService != null ? apiService.hashCode() : 0);
        return result;
    }
}