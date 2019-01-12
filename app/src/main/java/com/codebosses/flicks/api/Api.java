package com.codebosses.flicks.api;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {
    //        String APPLICATION_JSON_CHARSET_UTF_8 = "application/json; charset=utf-8";
//    String XWWWORMURLENCODED = "application/x-www-form-urlencoded";
    String APPLICATION_JSON_CHARSET_UTF_8 = "application/json; charset=utf-8";

    OkHttpClient.Builder httpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response response = chain.proceed(chain.request());
            // Do anything with response here
            response.header("Content-Type", APPLICATION_JSON_CHARSET_UTF_8);
            response.header("Accept", APPLICATION_JSON_CHARSET_UTF_8);
            return response;
        }
    }).addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).readTimeout(120, TimeUnit.SECONDS).connectTimeout(120, TimeUnit.SECONDS).retryOnConnectionFailure(true);

    OkHttpClient client = httpClient.build();

    Api WEB_SERVICE = new Retrofit.Builder()
            .baseUrl("https://fennix.site/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build().create(Api.class);

//    @FormUrlEncoded
//    @POST("api/login")
//    Call<LoginSignup> login(@Header("secret_key") String secret_key, @Field("email") String email, @Field("password") String password, @Field("device_token") String device_token, @Field("device_type") String device_type);
//
//    @FormUrlEncoded
//    @POST("api/register")
//    Call<LoginSignup> signup(@Header("secret_key") String secret_key, @Field("full_name") String full_name, @Field("email") String email, @Field("password") String password, @Field("device_token") String device_token, @Field("device_type") String device_type);
//
//    @FormUrlEncoded
//    @POST("api/forgotpassword")
//    Call<LoginSignup> forgotpassword(@Header("secret_key") String secret_key, @Field("email") String email);
//
//    @FormUrlEncoded
//    @POST("api/facebook_login")
//    Call<LoginSignup> facebooklogin(@Header("secret_key") String secret_key, @Field("full_name") String full_name, @Field("email") String email, @Field("facebook_uid") String facebook_uid, @Field("device_token") String device_token, @Field("device_type") String device_type);
//
//    @FormUrlEncoded
//    @POST("api/referral")
//    Call<GenericModelStatusMessage> refer(@Header("secret_key") String secret_key, @Field("user_id") String user_id, @Field("email") String email, @Field("full_name") String full_name, @Field("site_name") String site_name, @Field("phone") String phone);
//
//    @FormUrlEncoded
//    @POST("api/send_mail")
//    Call<GenericModelStatusMessage> email(@Header("secret_key") String secret_key, @Field("user_id") String user_id, @Field("email") String email, @Field("phone") String phone);
//
//    @Multipart
//    @POST("api/update_profile")
//    Call<LoginSignup> editProfile(
//            @Header("secret_key") String secret_key,
//            @Part MultipartBody.Part filePart,
//            @Part("first_name") RequestBody firstName,
//            @Part("last_name") RequestBody lastName,
//            @Part("user_id") RequestBody user_id,
//            @Part("email") RequestBody email,
//            @Part("password") RequestBody password
//    );
//
//    @FormUrlEncoded
//    @POST("api/getusersites")
//    Call<UserSitesModel> getUserSites(@Header("secret_key") String secret_key, @Field("user_id") String user_id);
//
//    @FormUrlEncoded
//    @POST("api/gettemplates")
//    Call<UserTemplates> getTemplates(@Header("secret_key") String secret_key, @Field("user_id") String user_id);
//
//    @FormUrlEncoded
//    @POST("api/createsite")
//    Call<CreateSiteModel> createSite(@Header("secret_key") String secret_key, @Field("user_id") String user_id, @Field("site_name") String site_name);
//
//    @FormUrlEncoded
//    @POST("api/buildsite")
//    Call<BuildSiteModel> buildSite(@Header("secret_key") String secret_key, @Field("page_id") String page_id, @Field("site_id") String site_id, @Field("type") String type, @Field("new_page_id") String new_page_id);
//
//    @FormUrlEncoded
//    @POST("api/pushPageBlock")
//    Call<EditTemplateModel> loadTemplateBlocks(@Header("secret_key") String secret_key, @Field("site_id") String site_id, @Field("page_id") String pageid);
//
//    @FormUrlEncoded
//    @POST("api/getBlocksEditPage")
//    Call<BlocksComponentsModel> blocks(@Header("secret_key") String secret_key, @Field("user_id") String user_id);
//
//    @FormUrlEncoded
//    @POST("api/getComponentsEditPage")
//    Call<BlocksComponentsModel> components(@Header("secret_key") String secret_key, @Field("component_fetch") String componentsFetch);
//
//    @Multipart
//    @POST("api/uploadImage")
//    Call<TemplateImageUploadModel> uploadTemplateImage(
//            @Header("secret_key") String secret_key,
//            @Part MultipartBody.Part filePart
//    );
//
//    @FormUrlEncoded
//    @POST("api/getBlocksEditPageHtml")
//    Call<BlocksHtmlObject> getBlockHtml(@Header("secret_key") String secret_key, @Field("blocks_id") String block_id);
//
//    @FormUrlEncoded
//    @POST("api/saveHtmlEditPage")
//    Call<GenericModelStatusMessage> saveEditedTemplate(@Header("secret_key") String secret_key, @Field("updated_html") String data, @Field("save_temp_edit_page") String req, @Field("screenshotflag") String flag);
//
//    @FormUrlEncoded
//    @POST("api/creatSiteNewPage")
//    Call<CreatePageObject> createNewPage(@Header("secret_key") String secret_key, @Field("site_id") String site_id, @Field("page_name") String page_name);
//
//    @FormUrlEncoded
//    @POST("api/sitePagesList")
//    Call<SitePagesObject> getSitePages(@Header("secret_key") String secret_key, @Field("site_id") String site_id);
//
//    @FormUrlEncoded
//    @POST("chat/get_users")
//    Call<ChatConversationObject> getChatConversation(@Header("secret_key") String secret_key, @Field("user_id") String user_id);
//
//    @FormUrlEncoded
//    @POST("chat/get_conversation")
//    Call<ChatDetailObject> getUserChat(@Header("secret_key") String secret_key, @Field("user_id") String user_id, @Field("email") String email);
//
//    @FormUrlEncoded
//    @POST("chat/send_message")
//    Call<ChatDetailObject> send_message(@Header("secret_key") String secret_key, @Field("message") String message, @Field("sent_by") String sent_by, @Field("sent_to") String sent_to, @Field("session_id") String session_id, @Field("ip_address") String ip_address, @Field("sender_name") String sender_name);
//
//    @FormUrlEncoded
//    @POST("api/notificationOnoff")
//    Call<GenericModelStatusMessage> notification_on_off(@Header("secret_key") String secret_key, @Field("user_id") String user_id, @Field("notification") String notification);
//
//    @FormUrlEncoded
//    @POST("chat/chatOnoff")
//    Call<GenericModelStatusMessage> chat_on_off(@Header("secret_key") String secret_key, @Field("user_id") String user_id, @Field("chat") String chat);
//
//    @FormUrlEncoded
//    @POST("api/logout")
//    Call<GenericModelStatusMessage> log_out(@Header("secret_key") String secret_key, @Field("user_id") String user_id, @Field("device_type") String device_type, @Field("device_token") String device_token);

//    @GET("items.php/get_categories")
//    Call<HomeBuyerModel> buyerGetCategories();
//
//
//    @FormUrlEncoded
//    @POST("items.php/like_item")
//    Call<LikeApiModel>buyerLike(@Field("user_id") String user_id, @Field("item_id") String item_id, @Field("is_like") String is_like);
//
//
//    @FormUrlEncoded
//    @POST("items.php/unlike_item")
//    Call<LikeApiModel>buyerUnLike(@Field("user_id") String user_id, @Field("item_id") String item_id, @Field("is_like") String is_like);
//
//
//
//    @GET()
//    Call<HomeDetailModel> buyerGetCategoriesDetail(@Url String url);
//
//    @GET()
//    Call<ItemComment> buyerGetItemComment(@Url String url);
//
//    @FormUrlEncoded
//    @POST("items.php/add_cart_item")
//    Call<LikeApiModel>buyerAddToCart(@Field("buyer_id") String buyer_id, @Field("item_id") String item_id);
//
//
//
//    @GET()
//    Call<CartModel> buyerGetCart(@Url String url);
//
//    @GET()
//    Call<TruckModel> buyerGetTrucks(@Url String url);
//
//    @GET()
//    Call<HomeDetailModel> buyerTrucksDishes(@Url String url);
//
//    @FormUrlEncoded
//    @POST("items.php/add_to_fev")
//    Call<LikeApiModel>buyerAddFavourite(@Field("user_id") String user_id, @Field("item_id") String item_id);
//
//
//    @POST("order.php/check_out")
//    Call<OrderSumbitModel> RequestOrderSumbit(@Body InputOrderSumbit body);
//
//
//
//    @GET()
//    Call<HomeDetailModel> buyerGetFavouriteItems(@Url String url);
//
//    @GET()
//    Call<MyOrders> buyerGetMyOrders(@Url String url);
//
//    @Multipart
//    @POST("user.php/update_profile_image")
//    Call<ProfileUploadApiModel> buyerProfilePic(@Part MultipartBody.Part filePart, @Part("user_id") RequestBody user_id);
//
//
//    @FormUrlEncoded
//    @POST("messages.php/send_message")
//    Call<LikeApiModel>AddComment(@Field("buyer_id") String buyer_id, @Field("seller_id") String seller_id, @Field("message") String message, @Field("sender_type") String sender_type);
//
//
//    @GET()
//    Call<BothChats> buyerGetChats(@Url String url);
//
//    @GET()
//    Call<BothMessages> buyerGetMessages(@Url String url);
//
//    @FormUrlEncoded
//    @POST("user.php/update_profile")
//    Call<Profile>buyerProfileSave(@Field("user_id") String user_id, @Field("name") String name, @Field("email") String email, @Field("mobile") String mobile, @Field("details") String details);
//
//
//    @FormUrlEncoded
//    @POST("user.php/change_password")
//    Call<LikeApiModel>buyerChangePassword(@Field("user_id") String user_id, @Field("old_password") String old_password, @Field("password") String password);
//
//
//    @FormUrlEncoded
//    @POST("user.php/logout")
//    Call<LikeApiModel>buyerLogout(@Field("user_id") String user_id);
//
//
//    @FormUrlEncoded
//    @POST("user.php/change_notify")
//    Call<LikeApiModel>buyerNoti(@Field("user_id") String user_id, @Field("notify") String notify);
//
//    @GET()
//    Call<Chats> buyerGetThreads(@Url String url);
//
////    Seller Apis   //////////////////////////////////////////////////////////////////////////////////////////////////
//
//
//    @FormUrlEncoded
//    @POST("user.php/buyer_register")
//    Call<Registration>sellerRegistration(@Field("name") String name, @Field("username") String username, @Field("email") String email, @Field("password") String password, @Field("mobile") String mobile, @Field("seller") String seller, @Field("food_truck") String food_truck, @Field("token") String token, @Field("device") String device);
//
//
//    @FormUrlEncoded
//    @POST("user.php/login")
//    Call<Registration>sellerrLogin(@Field("email") String email, @Field("password") String password);
//
//
//
//    @GET()
//    Call<ManegeOrders> selerGetOrders(@Url String url);
//
//    @GET()
//    Call<MyItems> sellerGetItems(@Url String url);
//
//
//
//    @FormUrlEncoded
//    @POST("items.php/delete_item")
//    Call<LikeApiModel>sellerrDellMyItem(@Field("user_id") String user_id, @Field("item_id") String item_id);
//
//
//    @Multipart
//    @POST("items.php/add_item")
//    Call<LikeApiModel> RequestUploadItem(@Part MultipartBody.Part[] filePart, @Part("user_id") RequestBody user_id, @Part("category_id") RequestBody category_id, @Part("title") RequestBody title, @Part("description") RequestBody description, @Part("price") RequestBody price, @Part("delivery_cost") RequestBody delivery_cost, @Part("status") RequestBody status);
//
//    @Multipart
//    @POST("items.php/update_item/")
//    Call<LikeApiModel> RequestUpdateItem(@Part MultipartBody.Part[] filePart, @Part("user_id") RequestBody user_id, @Part("category_id") RequestBody category_id, @Part("title") RequestBody title, @Part("description") RequestBody description, @Part("price") RequestBody price, @Part("delivery_cost") RequestBody delivery_cost);
//
//    @FormUrlEncoded
//    @POST("order.php/set_order_status")
//    Call<LikeApiModel>sellerSetStatus(@Field("order_id") String order_id, @Field("status") String status);
//


//
//    @FormUrlEncoded
//    @POST("usergroups")
//    Call<FcmApiResponseModel> GroupSave(@Field("roomname") String roomname, @Field("roomjid") String roomjid, @Field("user_id") String user_id, @Field("role") String role, @Field("description") String description, @Field("color") String color);
//
//
//    @GET()
//    Call<GroupGet> RequestGroupFetch(@Url String url);
//

//    @Multipart
//    @POST()
//    Call<ProfilePicUpdateModel> ProfilePicUpdate(@Url String url, @Part MultipartBody.Part filePart);
//


//
//
//    @DELETE()
//    Call<DeleteFromCart> RequestDeleteFromCart(@Url String url);
//
//    @GET()
//    Call<UserTemplates> RequestGetWishList(@Url String url);
//
//    @POST("wishlist")
//    Call<AddToWishList> RequestAddToWishList(@Body WishListJsonModel body);
//
//    @GET()
//    Call<FeatureItems> RequestProductFeaturePagination(@Url String url);
//
//    @GET()
//    Call<DepartmentsItems> RequestProductPagination(@Url String url);
//
//
//    @GET("product")
//    Call<VoiceSearchModel> RequestVoiceSearch(@Query("keyword") String Word);
//
//    @POST("recently_viewed")
//    Call<AddToWishList> RequestRecentViewedAdd(@Body InputRecentViewedJson body);
//
//
//    @GET()
//    Call<RecentViewedItems> RequestRecentViewed(@Url String url);
//

//

//    @DELETE()
//    Call<AdminDeleteProductModel> RequestDeleteProduct(@Url String url);
//
//    @GET()
//    Call<AiDealsModel> RequestAiDealsPagination(@Url String url);
//
//    @GET()
//    Call<TodayDealModel> RequestTodaydealPagination(@Url String url);
//
//    @Multipart
//    @POST("product/search_products_by_image")
//    Call<ImageSearch> RequestImageSearch(@Part MultipartBody.Part filePart);
//
//    @POST("update_account_info")
//    Call<UpdateAccountResponseModel> RequestUpdateAccountInfo(@Body UpdateAccountInfoModel body);
//
//    @GET()
//    Call<Test> RequestYourOrders(@Url String url);
//
//    @GET()
//    Call<com.leadconcept.boai.Model.kashif.Test> RequestOrdersReceived(@Url String url);
//
//    @POST("order/update_order")
//    Call<UpdateOrderResponseModel> RequestUpdateOrder(@Body UpdateOrderJsonModel body);
//
//
//    @POST("user")
//    Call<AdminDeleteProductModel> RequestAddUser(@Body InputJsonAdminAddUser body);
//
//    @Multipart
//    @POST("product/uplod_3d_image")
//    Call<AddToWishList> RequestUpload360Product(@Part MultipartBody.Part[] filePart, @Part("product_id") RequestBody product_id);
//
//
//
//

//    @FormUrlEncoded
//    @PUT()
//    Call<ProfileChange> updateProfileDetail(@Url String url, @Field("full_name") String full_name, @Field("email") String email, @Field("gender") String gender, @Field("phone_number") String phone_number, @Field("country") String country, @Field("theme_name") String theme_name, @Field("personal_info") String personal_info);
//
//
//    @FormUrlEncoded
//    @POST("groups")
//    Call<FcmApiResponseModel> GroupsSave(@Field("roomname") String roomname, @Field("roomjid") String roomjid, @Field("user_id") String user_id, @Field("role") String role, @Field("description") String description, @Field("color") String color);
//
//    @FormUrlEncoded
//    @POST("groups/admin")
//    Call<OpenChatAdminCheck> checkForAdmin(@Field("roomjid") String roomjid, @Field("user_id") String user_id);
//
//    @GET()
//    Call<ModelKickout> RequestKickout(@Url String url);
//
//    @POST("update_group")
//    Call<GroupInfoUpdateResponseModel> UpdateRoomInfoReq(@Body InputJsonRoomInfoUpdate body);
//
//    @POST("get_group_data")
//    Call<GroupInfoUpdatedResponse> UpdatedRoomInfoReq(@Body InputJsonRoomInfo body);
//
//    @Multipart
//    @POST()
//    Call<FcmApiResponseModel> requestUploadGroupSave(@Url String url,
//                                                     @Part MultipartBody.Part filePart,
//                                                     @Part("roomname") RequestBody roomname,
//                                                     @Part("roomjid") RequestBody roomjid,
//                                                     @Part("user_id") RequestBody user_id,
//                                                     @Part("role") RequestBody role,
//                                                     @Part("description") RequestBody description,
//                                                     @Part("color") RequestBody color
//    );
//

//    @FormUrlEncoded
//    @POST ("customers.php")
//    Call<CustomerModel> ReqCustomer(@Header("token") String token, @Field("company_id") String company_id);
//
//    @FormUrlEncoded
//    @POST("exchange_rate.php")
//    Call<RateModel> ReqExchangeRates(@Header("token") String token, @Field("company_id") String company_id);
//
//    @POST("logout.php")
//    Call<LogoutModel> ReqLogout(@Header("token") String token);
//
//    @FormUrlEncoded
//    @POST("sales.php")
//    Call<LogoutModel> ReqTransaction(@Header("token") String token, @Field("json") String json);

//    @GET("dancetypes")
//    Call<UserTemplates> getCategories(@Header("Authorizuser") String token);
//
//    @GET("events")
//    Call<E> getEvents(@Header("Authorizuser") String token, @Query("cat") String cat, @Query("subcat") String subcat);
//
//    @GET("members")
//    Call<UserModel> getMembers(@Header("oauth_secret") String oauth_secret, @Header("oauth_token") String oauth_token, @Header("oauth_consumer_key") String oauth_consumer_key, @Header("oauth_consumer_secret") String oauth_consumer_secret, @Query("page") String mPageNo);

    //    @GET("api/live-streams-videos")
//    Call<HomeVideos> getHomeVideos(@Header("Authorization") String token);
//    @GET("api/live-streams-videos")
//    Call<HomeVideos> getHomeVideos();
//    @FormUrlEncoded
//    @POST("login.php")
//    Call<ResponseModel> registerUser(@FieldMap Map<String, String> fields);
//

//
//    @FormUrlEncoded
//    @POST("api/accounts/social-login/")
//    Call<ResponseModel> loginSocialUser(@Field("provider") String provider,
//                                        @Field("access_token") String access_token,
//                                        @Field("access_token_secret") String access_token_secret);
//
//    @FormUrlEncoded
//    @POST("api/accounts/forget-password/")
//    Call<ResponseModel> forgotPassword(@Field("email") String email);
//
//    @GET("api/user/{userId}")
//    Call<UserProfile> getUserProfileData(@Path(value = "userId", encoded = false) String userId);
//
//    @GET("api/categories")
//    Call<Department> getCategories();
//
//    @GET("api/department/streams-videos/{categorySlug}")
//    Call<Department> getCategoryItems(@Header("Authorization") String token, @Path(value = "categorySlug", encoded = false) String categorySlug);
//
//    @FormUrlEncoded
//    @POST("api/follow-unfollow-user/")
//    Call<Follow> followUser(@Header("Authorization") String token, @Field("user_id") String user_id);
//
//    @FormUrlEncoded
//    @POST("api/check-follow-status/")
//    Call<ResponseModel> checkFollowStats(@Header("Authorization") String token, @Field("user_id") String user_id);
//
//    @FormUrlEncoded
//    @POST("api/update-stream-video-likes/")
//    Call<LikeCount> likeVideo(@Header("Authorization") String token, @Field("vos_id") String vos_id, @Field("likes") String likes);
//
//
//    @GET("api/user-total-likes/{user_id}")
//    Call<LikeCount> getUserLikes(@Path(value = "user_id", encoded = false) String user_id);
//
//    @FormUrlEncoded
//    @PUT("api/accounts/edit_information/")
//    Call<ResponseModel> updateProfileImage(@Header("Authorization") String token, @Field("image") String image);
}