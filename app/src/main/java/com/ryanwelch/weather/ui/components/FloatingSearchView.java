package com.ryanwelch.weather.ui.components;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ryanwelch.weather.R;
import com.ryanwelch.weather.domain.models.SearchSuggestion;
import com.ryanwelch.weather.ui.searchscreen.SearchSuggestionsAdapter;
import com.ryanwelch.weather.ui.helpers.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FloatingSearchView extends FrameLayout {

    private static final int CARD_VIEW_TOP_BOTTOM_SHADOW_HEIGHT = 3;
    private static final int CARD_VIEW_CORNERS_AND_TOP_BOTTOM_SHADOW_HEIGHT = 5;
    private static final long CLEAR_BTN_FADE_ANIM_DURATION = 250;
    private static final int CLEAR_BTN_WIDTH = 48;

    private final int SUGGESTION_ANIM_DURATION = 100;
    private final Interpolator SUGGEST_ITEM_ADD_ANIM_INTERPOLATOR = new LinearInterpolator();

    private Activity mHostActivity;

    private View mMainLayout;
    @BindView(R.id.search_input_parent) View mSearchInputParent;
    @BindView(R.id.search_query_section) CardView mQuerySection;
    @BindView(R.id.search_bar_text) SearchInputView mSearchInput;
    @BindView(R.id.left_action) ImageView mLeftAction;
    @BindView(R.id.search_bar_search_progress) ProgressBar mSearchProgress;
    @BindView(R.id.clear_btn) ImageView mClearButton;
    @BindView(R.id.divider) View mDivider;
    @BindView(R.id.search_suggestions_section) RelativeLayout mSuggestionsSection;
    @BindView(R.id.suggestions_list_container) View mSuggestionListContainer;
    @BindView(R.id.suggestions_list) RecyclerView mSuggestionsList;

    private Drawable mIconBackArrow;
    private Drawable mIconSearch;
    private Drawable mIconClear;

    private OnFocusChangeListener mFocusChangeListener;
    private OnSearchListener mSearchListener;
    private OnQueryChangeListener mQueryListener;
    private OnHomeActionClickListener mOnHomeActionClickListener;

    private boolean mDismissOnOutsideTouch = true;
    private boolean mIsFocused = false;
    private boolean mCloseSearchOnSoftKeyboardDismiss = true;
    private boolean mShowSearchKey = true;
    private boolean mSkipQueryFocusChangeEvent;
    private boolean mSkipTextChangeEvent;
    private String mOldQuery = "";
    private String mSearchHint;

    private SearchSuggestionsAdapter mSuggestionsAdapter;
    private SearchSuggestionsAdapter.OnBindSuggestionCallback mOnBindSuggestionCallback;

    private boolean mIsInitialLayout = true;

    private boolean mIsSuggestionsSecHeightSet;
    private OnSuggestionSecHeightSetListener mSuggestionSecHeightListener;

    private interface OnSuggestionSecHeightSetListener {
        void onSuggestionSecHeightSet();
    }

    public interface OnQueryChangeListener {

        void onSearchTextChanged(String oldQuery, String newQuery);
    }

    public interface OnSearchListener {

        void onSuggestionClicked(SearchSuggestion searchSuggestion);

        //void onSearchAction(String currentQuery);
    }

    public interface OnFocusChangeListener {

        void onFocus();

        void onFocusCleared();
    }

    public interface OnHomeActionClickListener {

        void onHomeAction();
    }

    public FloatingSearchView(Context context) {
        this(context, null);
    }

    public FloatingSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mHostActivity = getHostActivity();

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMainLayout = layoutInflater.inflate(R.layout.view_floating_search, this);

        ButterKnife.bind(this, mMainLayout);

        mIconClear = ContextCompat.getDrawable(getContext(), R.drawable.ic_clear_black);
        mIconBackArrow = ContextCompat.getDrawable(getContext(), R.drawable.ic_arrow_back_black);
        mIconSearch = ContextCompat.getDrawable(getContext(), R.drawable.ic_search_black);
        mClearButton.setImageDrawable(mIconClear);

        setupViews(attrs);
    }

    private Activity getHostActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (mIsInitialLayout) {
            //we need to add 5dp to the mSuggestionsSection because we are
            //going to move it up by 5dp in order to cover the search bar's
            //shadow padding and rounded corners. We also need to add an additional 10dp to
            //mSuggestionsSection in order to hide mSuggestionListContainer's
            //rounded corners and shadow for both, top and bottom.
            int addedHeight = 3 * Util.dpToPx(CARD_VIEW_CORNERS_AND_TOP_BOTTOM_SHADOW_HEIGHT);
            final int finalHeight = mSuggestionsSection.getHeight() + addedHeight;
            mSuggestionsSection.getLayoutParams().height = finalHeight;
            mSuggestionsSection.requestLayout();
            ViewTreeObserver vto = mSuggestionListContainer.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    if (mSuggestionsSection.getHeight() == finalHeight) {
                        Util.removeGlobalLayoutObserver(mSuggestionListContainer, this);

                        mIsSuggestionsSecHeightSet = true;
                        moveSuggestListToInitialPos();
                        if (mSuggestionSecHeightListener != null) {
                            mSuggestionSecHeightListener.onSuggestionSecHeightSet();
                            mSuggestionSecHeightListener = null;
                        }
                    }
                }
            });

            mIsInitialLayout = false;
        }
    }

    private void setupViews(AttributeSet attrs) {
       // mSuggestionsSection.setEnabled(true);

        final int searchBarLeftMargin = Util.dpToPx(5);
        final int searchBarRightMargin = Util.dpToPx(5);
        final int searchBarTopMargin = Util.dpToPx(10);

        LayoutParams querySectionLP = (LayoutParams) mQuerySection.getLayoutParams();
        LayoutParams dividerLP = (LayoutParams) mDivider.getLayoutParams();
        LinearLayout.LayoutParams suggestListSectionLP =
                (LinearLayout.LayoutParams) mSuggestionsSection.getLayoutParams();
        int cardPadding = Util.dpToPx(CARD_VIEW_TOP_BOTTOM_SHADOW_HEIGHT);
        querySectionLP.setMargins(searchBarLeftMargin, searchBarTopMargin,
                searchBarRightMargin, 0);
        dividerLP.setMargins(searchBarLeftMargin + cardPadding, 0,
                searchBarRightMargin + cardPadding,
                ((MarginLayoutParams) mDivider.getLayoutParams()).bottomMargin);
        suggestListSectionLP.setMargins(searchBarLeftMargin, 0, searchBarRightMargin, 0);
        mQuerySection.setLayoutParams(querySectionLP);
        mDivider.setLayoutParams(dividerLP);
        mSuggestionsSection.setLayoutParams(suggestListSectionLP);

        setupQueryBar();

        if (!isInEditMode()) {
            setupSuggestionSection();
        }
    }

    private void setupQueryBar() {
        if (!isInEditMode() && mHostActivity != null) {
            mHostActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        }

        mClearButton.setVisibility(View.INVISIBLE);
        mClearButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchInput.setText("");
            }
        });

        mSearchInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                //todo investigate why this is called twice when pressing back on the keyboard

                if (mSkipTextChangeEvent || !mIsFocused) {
                    mSkipTextChangeEvent = false;
                } else {
                    if (mSearchInput.getText().toString().length() != 0 &&
                            mClearButton.getVisibility() == View.INVISIBLE) {
                        mClearButton.setAlpha(0.0f);
                        mClearButton.setVisibility(View.VISIBLE);
                        ViewCompat.animate(mClearButton).alpha(1.0f).setDuration(CLEAR_BTN_FADE_ANIM_DURATION).start();
                    } else if (mSearchInput.getText().toString().length() == 0) {
                        mClearButton.setVisibility(View.INVISIBLE);
                    }

                    if (mQueryListener != null && mIsFocused && !mOldQuery.equals(mSearchInput.getText().toString())) {
                        mQueryListener.onSearchTextChanged(mOldQuery, mSearchInput.getText().toString());
                    }

                }

                mOldQuery = mSearchInput.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        mSearchInput.setOnFocusChangeListener(new TextView.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mSkipQueryFocusChangeEvent) {
                    mSkipQueryFocusChangeEvent = false;
                } else if (hasFocus != mIsFocused) {
                    setSearchFocusedInternal(hasFocus);
                }
            }
        });

        mSearchInput.setOnKeyboardDismissedListener(new SearchInputView.OnKeyboardDismissedListener() {
            @Override
            public void onKeyboardDismissed() {
                if (mCloseSearchOnSoftKeyboardDismiss) {
                    setSearchFocusedInternal(false);
                }
            }
        });

        mSearchInput.setOnSearchKeyListener(new SearchInputView.OnKeyboardSearchKeyClickListener() {
            @Override
            public void onSearchKeyClicked() {
                mSkipTextChangeEvent = true;
                setSearchFocusedInternal(false);
            }
        });

        mLeftAction.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSearchFocusedInternal(false);
                if (mOnHomeActionClickListener != null) {
                    mOnHomeActionClickListener.onHomeAction();
                }
            }
        });

        mLeftAction.setVisibility(VISIBLE);
        mLeftAction.setImageDrawable(mIconSearch);
    }

    private void refreshSearchInputPaddingEnd(int menuItemsWidth) {
        if (menuItemsWidth == 0) {
            mClearButton.setTranslationX(-Util.dpToPx(4));
            int paddingRight = Util.dpToPx(4);
            if (mIsFocused) {
                paddingRight += Util.dpToPx(CLEAR_BTN_WIDTH);
            } else {
                paddingRight += Util.dpToPx(14);
            }
            mSearchInput.setPadding(0, 0, paddingRight, 0);
        } else {
            mClearButton.setTranslationX(-menuItemsWidth);
            int paddingRight = menuItemsWidth;
            if (mIsFocused) {
                paddingRight += Util.dpToPx(CLEAR_BTN_WIDTH);
            }
            mSearchInput.setPadding(0, 0, paddingRight, 0);
        }
    }

    public void showProgress() {
        mLeftAction.setVisibility(View.GONE);
        mSearchProgress.setAlpha(0.0f);
        mSearchProgress.setVisibility(View.VISIBLE);
        ObjectAnimator.ofFloat(mSearchProgress, "alpha", 0.0f, 1.0f).start();
    }

    public void hideProgress() {
        mSearchProgress.setVisibility(View.GONE);
        mLeftAction.setAlpha(0.0f);
        mLeftAction.setVisibility(View.VISIBLE);
        ObjectAnimator.ofFloat(mLeftAction, "alpha", 0.0f, 1.0f).start();
    }

    public void setSearchHint(String searchHint) {
        mSearchHint = searchHint != null ? searchHint : getResources().getString(R.string.abc_search_hint);
        mSearchInput.setHint(mSearchHint);
    }

    public void setShowSearchKey(boolean show) {
        mShowSearchKey = show;
        if (show) {
            mSearchInput.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        } else {
            mSearchInput.setImeOptions(EditorInfo.IME_ACTION_NONE);
        }
    }

    public void setCloseSearchOnKeyboardDismiss(boolean closeSearchOnKeyboardDismiss) {
        this.mCloseSearchOnSoftKeyboardDismiss = closeSearchOnKeyboardDismiss;
    }

    public void setDismissOnOutsideClick(boolean enable) {
        mDismissOnOutsideTouch = enable;
        mSuggestionsSection.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //todo check if this is called twice
                if (mDismissOnOutsideTouch && mIsFocused) {
                    setSearchFocusedInternal(false);
                }

                return true;
            }
        });
    }

    public String getQuery() {
        return mOldQuery;
    }

    public void clearQuery() {
        mSearchInput.setText("");
    }

    public boolean setSearchFocused(final boolean focused) {
        boolean updatedToNotFocused = !focused && this.mIsFocused;

        if ((focused != this.mIsFocused) && mSuggestionSecHeightListener == null) {
            if (mIsSuggestionsSecHeightSet) {
                setSearchFocusedInternal(focused);
            } else {
                mSuggestionSecHeightListener = new OnSuggestionSecHeightSetListener() {
                    @Override
                    public void onSuggestionSecHeightSet() {
                        setSearchFocusedInternal(focused);
                        mSuggestionSecHeightListener = null;
                    }
                };
            }
        }
        return updatedToNotFocused;
    }

    private void setupSuggestionSection() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, true);
        mSuggestionsList.setLayoutManager(layoutManager);
        mSuggestionsList.setItemAnimator(null);

//        final GestureDetector gestureDetector = new GestureDetector(getContext(),
//                new GestureDetectorListenerAdapter() {
//
//                    @Override
//                    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//                        if (mHostActivity != null) {
//                            Util.closeSoftKeyboard(mHostActivity);
//                        }
//                        return false;
//                    }
//                });
//
//        mSuggestionsList.addOnItemTouchListener(new OnItemTouchListenerAdapter() {
//            @Override
//            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//                gestureDetector.onTouchEvent(e);
//                return false;
//            }
//        });

        mSuggestionsAdapter = new SearchSuggestionsAdapter(getContext(),
                new SearchSuggestionsAdapter.Listener() {

                    @Override
                    public void onItemSelected(SearchSuggestion item) {
                        mIsFocused = false;

                        if (mSearchListener != null) {
                            mSearchListener.onSuggestionClicked(item);
                        }

                        mSkipTextChangeEvent = true;
                        setSearchFocusedInternal(false);
                    }
                });

        mSuggestionsList.setAdapter(mSuggestionsAdapter);

        int cardViewBottomPadding = Util.dpToPx(CARD_VIEW_CORNERS_AND_TOP_BOTTOM_SHADOW_HEIGHT);
        //move up the suggestions section enough to cover the search bar
        //card's bottom left and right corners
        mSuggestionsSection.setTranslationY(-cardViewBottomPadding);

        moveSuggestListToInitialPos();
        mSuggestionsSection.setVisibility(VISIBLE);
    }

    private void moveSuggestListToInitialPos() {
        //move the suggestions list to the collapsed position
        //which is translationY of -listContainerHeight
        mSuggestionListContainer.setTranslationY(-mSuggestionListContainer.getHeight());
    }

    public void swapSuggestions(final List<? extends SearchSuggestion> newSearchSuggestions) {
        Collections.reverse(newSearchSuggestions);
        swapSuggestions(newSearchSuggestions, true);
    }

    private void swapSuggestions(final List<? extends SearchSuggestion> newSearchSuggestions,
                                 final boolean withAnim) {

        mSuggestionsList.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Util.removeGlobalLayoutObserver(mSuggestionsList, this);
                updateSuggestionsSectionHeight(newSearchSuggestions, withAnim);
            }
        });
        mSuggestionsAdapter.setItems(newSearchSuggestions);

        mDivider.setVisibility(!newSearchSuggestions.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void updateSuggestionsSectionHeight(List<? extends SearchSuggestion>
                                                        newSearchSuggestions, boolean withAnim) {

        final int cardTopBottomShadowPadding = Util.dpToPx(CARD_VIEW_CORNERS_AND_TOP_BOTTOM_SHADOW_HEIGHT);
        final int cardRadiusSize = Util.dpToPx(CARD_VIEW_TOP_BOTTOM_SHADOW_HEIGHT);


        int visibleSuggestionHeight = calculateSuggestionItemsHeight(newSearchSuggestions,
                mSuggestionListContainer.getHeight());
        int diff = mSuggestionListContainer.getHeight() - visibleSuggestionHeight;
        int addedTranslationYForShadowOffsets = (diff <= cardTopBottomShadowPadding) ?
                -(cardTopBottomShadowPadding - diff) :
                diff < (mSuggestionListContainer.getHeight() - cardTopBottomShadowPadding) ? cardRadiusSize : 0;
        final float newTranslationY = -mSuggestionListContainer.getHeight() +
                visibleSuggestionHeight + addedTranslationYForShadowOffsets;

        final boolean animateAtEnd = newTranslationY >= mSuggestionListContainer.getTranslationY();

        ViewCompat.animate(mSuggestionListContainer).cancel();
        if (withAnim) {
            ViewCompat.animate(mSuggestionListContainer)
                    .setInterpolator(SUGGEST_ITEM_ADD_ANIM_INTERPOLATOR)
                    .setDuration(SUGGESTION_ANIM_DURATION)
                    .translationY(newTranslationY)
                    .setListener(new ViewPropertyAnimatorListenerAdapter() {
                        @Override
                        public void onAnimationCancel(View view) {
                            mSuggestionListContainer.setTranslationY(newTranslationY);
                        }

                        @Override
                        public void onAnimationStart(View view) {
                            if (!animateAtEnd) {
                                mSuggestionsList.smoothScrollToPosition(0);
                            }
                        }

                        @Override
                        public void onAnimationEnd(View view) {
                            if (animateAtEnd) {
                                int lastPos = mSuggestionsList.getAdapter().getItemCount() - 1;
                                if (lastPos > -1) {
                                    mSuggestionsList.smoothScrollToPosition(lastPos);
                                }
                            }
                        }
                    }).start();
        } else {
            mSuggestionListContainer.setTranslationY(newTranslationY);
        }
    }

    //returns the cumulative height that the current suggestion items take up or the given max if the
    //results is >= max. The max option allows us to avoid doing unnecessary and potentially long calculations.
    private int calculateSuggestionItemsHeight(List<? extends SearchSuggestion> suggestions, int max) {
        int visibleItemsHeight = 0;
        for (int i = 0; i < suggestions.size() && i < mSuggestionsList.getChildCount(); i++) {
            visibleItemsHeight += mSuggestionsList.getChildAt(i).getHeight();
            if (visibleItemsHeight > max) {
                visibleItemsHeight = max;
                break;
            }
        }
        return visibleItemsHeight;
    }

    public void setOnBindSuggestionCallback(SearchSuggestionsAdapter.OnBindSuggestionCallback callback) {
        this.mOnBindSuggestionCallback = callback;
        if (mSuggestionsAdapter != null) {
            mSuggestionsAdapter.setOnBindSuggestionCallback(mOnBindSuggestionCallback);
        }
    }

    public void clearSuggestions() {
        swapSuggestions(new ArrayList<SearchSuggestion>());
    }

    public boolean isSearchBarFocused() {
        return mIsFocused;
    }

    public void showSoftKeyboard(final Context context, final EditText editText) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 100);
    }

    public void closeSoftKeyboard(Activity activity) {
        View currentFocusView = activity.getCurrentFocus();
        if (currentFocusView != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(currentFocusView.getWindowToken(), 0);
        }
    }


    private void setSearchFocusedInternal(final boolean focused) {
        this.mIsFocused = focused;

        if (focused) {
            mSearchInput.requestFocus();
            //moveSuggestListToInitialPos();
            //mSuggestionsSection.setVisibility(VISIBLE);
            refreshSearchInputPaddingEnd(0);//this must be called before  mMenuView.hideIfRoomItems(...)
            showSoftKeyboard(getContext(), mSearchInput);
            mClearButton.setVisibility((mSearchInput.getText().toString().length() == 0) ?
                    View.INVISIBLE : View.VISIBLE);
            if (mFocusChangeListener != null) {
                mFocusChangeListener.onFocus();
            }
        } else {
            mMainLayout.requestFocus();
            //clearSuggestions();
            refreshSearchInputPaddingEnd(0);//this must be called before  mMenuView.hideIfRoomItems(...)
            mClearButton.setVisibility(View.GONE);
            if (mHostActivity != null) {
                closeSoftKeyboard(mHostActivity);
            }
            if (mFocusChangeListener != null) {
                mFocusChangeListener.onFocusCleared();
            }
        }

        //if we don't have focus, we want to allow the client's views below our invisible
        //screen-covering view to handle touches
        //mSuggestionsSection.setEnabled(focused);
    }

    public void setOnQueryChangeListener(OnQueryChangeListener listener) {
        this.mQueryListener = listener;
    }

    public void setOnSearchListener(OnSearchListener listener) {
        this.mSearchListener = listener;
    }

    public void setOnFocusChangeListener(OnFocusChangeListener listener) {
        this.mFocusChangeListener = listener;
    }

    public void setOnHomeActionClickListener(OnHomeActionClickListener listener) {
        this.mOnHomeActionClickListener = listener;
    }

//    @Override
//    public Parcelable onSaveInstanceState() {
//        Parcelable superState = super.onSaveInstanceState();
//        SavedState savedState = new SavedState(superState);
//        savedState.suggestions = this.mSuggestionsAdapter.getItems();
//        savedState.isFocused = this.mIsFocused;
//        savedState.query = getQuery();
//        savedState.searchHint = this.mSearchHint;
//        savedState.dismissOnOutsideClick = this.mDismissOnOutsideTouch;
//        savedState.showSearchKey = this.mShowSearchKey;
//        savedState.dismissOnSoftKeyboardDismiss = this.mDismissOnOutsideTouch;
//        return savedState;
//    }
//
//    @Override
//    public void onRestoreInstanceState(Parcelable state) {
//        final SavedState savedState = (SavedState) state;
//        super.onRestoreInstanceState(savedState.getSuperState());
//
//        this.mIsFocused = savedState.isFocused;
//        setDismissOnOutsideClick(savedState.dismissOnOutsideClick);
//        setShowSearchKey(savedState.showSearchKey);
//        setSearchHint(savedState.searchHint);
//        setBackgroundColor(savedState.backgroundColor);
//        setCloseSearchOnKeyboardDismiss(savedState.dismissOnSoftKeyboardDismiss);
//
//        mSuggestionsSection.setEnabled(this.mIsFocused);
//        if (this.mIsFocused) {
//            mSkipTextChangeEvent = true;
//            mSkipQueryFocusChangeEvent = true;
//
//            mSuggestionsSection.setVisibility(VISIBLE);
//
//            //restore suggestions list when suggestion section's height is fully set
//            mSuggestionSecHeightListener = new OnSuggestionSecHeightSetListener() {
//                @Override
//                public void onSuggestionSecHeightSet() {
//                    swapSuggestions(savedState.suggestions, false);
//                    mSuggestionSecHeightListener = null;
//
//                    //todo refactor move to a better location
//                    transitionInLeftSection(false);
//                }
//            };
//
//            mClearButton.setVisibility((savedState.query.length() == 0) ? View.INVISIBLE : View.VISIBLE);
//            mLeftAction.setVisibility(View.VISIBLE);
//
//            Util.showSoftKeyboard(getContext(), mSearchInput);
//        }
//    }
//
//    static class SavedState extends BaseSavedState {
//
//        private ArrayList<? extends GetSearchSuggestionInteractor> suggestions = new ArrayList<>();
//        private boolean isFocused;
//        private String query;
//        private int suggestionTextSize;
//        private String searchHint;
//        private boolean dismissOnOutsideClick;
//        private boolean showMoveSuggestionUpBtn;
//        private boolean showSearchKey;
//        private boolean isTitleSet;
//        private int backgroundColor;
//        private int suggestionsTextColor;
//        private int queryTextColor;
//        private int searchHintTextColor;
//        private int actionOverflowMenueColor;
//        private int menuItemIconColor;
//        private int leftIconColor;
//        private int clearBtnColor;
//        private int suggestionUpBtnColor;
//        private int dividerColor;
//        private int menuId;
//        private int leftActionMode;
//        private boolean dimBackground;
//        private long suggestionsSectionAnimSuration;
//        private boolean dismissOnSoftKeyboardDismiss;
//
//        SavedState(Parcelable superState) {
//            super(superState);
//        }
//
//        private SavedState(Parcel in) {
//            super(in);
//            in.readList(suggestions, getClass().getClassLoader());
//            isFocused = (in.readInt() != 0);
//            query = in.readString();
//            suggestionTextSize = in.readInt();
//            searchHint = in.readString();
//            dismissOnOutsideClick = (in.readInt() != 0);
//            showMoveSuggestionUpBtn = (in.readInt() != 0);
//            showSearchKey = (in.readInt() != 0);
//            isTitleSet = (in.readInt() != 0);
//            backgroundColor = in.readInt();
//            suggestionsTextColor = in.readInt();
//            queryTextColor = in.readInt();
//            searchHintTextColor = in.readInt();
//            actionOverflowMenueColor = in.readInt();
//            menuItemIconColor = in.readInt();
//            leftIconColor = in.readInt();
//            clearBtnColor = in.readInt();
//            suggestionUpBtnColor = in.readInt();
//            dividerColor = in.readInt();
//            menuId = in.readInt();
//            leftActionMode = in.readInt();
//            dimBackground = (in.readInt() != 0);
//            suggestionsSectionAnimSuration = in.readLong();
//            dismissOnSoftKeyboardDismiss = (in.readInt() != 0);
//        }
//
//        @Override
//        public void writeToParcel(Parcel out, int flags) {
//            super.writeToParcel(out, flags);
//            out.writeList(suggestions);
//            out.writeInt(isFocused ? 1 : 0);
//            out.writeString(query);
//            out.writeInt(suggestionTextSize);
//            out.writeString(searchHint);
//            out.writeInt(dismissOnOutsideClick ? 1 : 0);
//            out.writeInt(showMoveSuggestionUpBtn ? 1 : 0);
//            out.writeInt(showSearchKey ? 1 : 0);
//            out.writeInt(isTitleSet ? 1 : 0);
//            out.writeInt(backgroundColor);
//            out.writeInt(suggestionsTextColor);
//            out.writeInt(queryTextColor);
//            out.writeInt(searchHintTextColor);
//            out.writeInt(actionOverflowMenueColor);
//            out.writeInt(menuItemIconColor);
//            out.writeInt(leftIconColor);
//            out.writeInt(clearBtnColor);
//            out.writeInt(suggestionUpBtnColor);
//            out.writeInt(dividerColor);
//            out.writeInt(menuId);
//            out.writeInt(leftActionMode);
//            out.writeInt(dimBackground ? 1 : 0);
//            out.writeLong(suggestionsSectionAnimSuration);
//            out.writeInt(dismissOnSoftKeyboardDismiss ? 1 : 0);
//        }
//
//        public static final Creator<SavedState> CREATOR
//                = new Creator<SavedState>() {
//            public SavedState createFromParcel(Parcel in) {
//                return new SavedState(in);
//            }
//
//            public SavedState[] newArray(int size) {
//                return new SavedState[size];
//            }
//        };
//    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        //remove any ongoing animations to prevent leaks
        ViewCompat.animate(mSuggestionListContainer).cancel();
    }
}
