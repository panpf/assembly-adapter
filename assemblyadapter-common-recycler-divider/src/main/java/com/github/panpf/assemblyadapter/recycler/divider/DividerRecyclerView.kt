package com.github.panpf.assemblyadapter.recycler.divider

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.panpf.assemblyadapter.common.recycler.divider.R
import com.github.panpf.assemblyadapter.recycler.divider.internal.DividerSize

open class DividerRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.recyclerViewStyle
) : RecyclerView(context, attrs, defStyleAttr) {

    private val dividerParams: DividerParams?

    var isFullSpanByPosition: IsFullSpanByPosition? = null
        set(value) {
            field = value
            resetDivider(layoutManager)
        }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DividerRecyclerView)
        dividerParams = DividerParams.fromAttrs(typedArray)
        typedArray.recycle()

        if (isInEditMode && layoutManager == null) {
            super.setLayoutManager(LinearLayoutManager(context))
        }
        resetDivider(layoutManager)
    }

    override fun setLayoutManager(layout: LayoutManager?) {
        super.setLayoutManager(layout)
        resetDivider(layout)
    }

    private fun resetDivider(layout: LayoutManager?) {
        val oldDividerItemDecorationIndex = (0 until itemDecorationCount).find {
            getItemDecorationAt(it) is ItemDecorationWrapper
        }
        if (oldDividerItemDecorationIndex != null) {
            removeItemDecorationAt(oldDividerItemDecorationIndex)
        }

        val dividerParams = dividerParams
        if (dividerParams != null) {
            val newDividerItemDecoration = when (layout) {
                //The is order cannot be exchanged
                is StaggeredGridLayoutManager -> {
                    dividerParams.createStaggeredGridDividerItemDecoration(
                        this,
                        isFullSpanByPosition
                    )
                }
                is GridLayoutManager -> {
                    dividerParams.createGridDividerItemDecoration(this)
                }
                is LinearLayoutManager -> {
                    dividerParams.createLinearDividerItemDecoration(this)
                }
                else -> null
            }
            if (newDividerItemDecoration != null) {
                if (oldDividerItemDecorationIndex != null) {
                    addItemDecoration(
                        ItemDecorationWrapper(newDividerItemDecoration),
                        oldDividerItemDecorationIndex
                    )
                } else {
                    addItemDecoration(ItemDecorationWrapper(newDividerItemDecoration))
                }
            }
        }
    }

    private class DividerParams(
        val dividerDrawable: Drawable?,
        val dividerSize: Int?,
        val dividerWidth: Int?,
        val dividerHeight: Int?,
        val dividerInsets: Int?,
        val dividerInsetStart: Int?,
        val dividerInsetTop: Int?,
        val dividerInsetEnd: Int?,
        val dividerInsetBottom: Int?,

        val headerDividerDrawable: Drawable?,
        val headerDividerSize: Int?,
        val headerDividerWidth: Int?,
        val headerDividerHeight: Int?,
        val headerDividerInsets: Int?,
        val headerDividerInsetStart: Int?,
        val headerDividerInsetTop: Int?,
        val headerDividerInsetEnd: Int?,
        val headerDividerInsetBottom: Int?,

        val footerDividerDrawable: Drawable?,
        val footerDividerSize: Int?,
        val footerDividerWidth: Int?,
        val footerDividerHeight: Int?,
        val footerDividerInsets: Int?,
        val footerDividerInsetStart: Int?,
        val footerDividerInsetTop: Int?,
        val footerDividerInsetEnd: Int?,
        val footerDividerInsetBottom: Int?,

        val useDividerAsHeaderDivider: Boolean?,
        val useDividerAsFooterDivider: Boolean?,
        val useDividerAsHeaderAndFooterDivider: Boolean?,

        val sideDividerDrawable: Drawable?,
        val sideDividerSize: Int?,
        val sideDividerWidth: Int?,
        val sideDividerHeight: Int?,
        val sideDividerInsets: Int?,
        val sideDividerInsetStart: Int?,
        val sideDividerInsetTop: Int?,
        val sideDividerInsetEnd: Int?,
        val sideDividerInsetBottom: Int?,

        val sideHeaderDividerDrawable: Drawable?,
        val sideHeaderDividerSize: Int?,
        val sideHeaderDividerWidth: Int?,
        val sideHeaderDividerHeight: Int?,
        val sideHeaderDividerInsets: Int?,
        val sideHeaderDividerInsetStart: Int?,
        val sideHeaderDividerInsetTop: Int?,
        val sideHeaderDividerInsetEnd: Int?,
        val sideHeaderDividerInsetBottom: Int?,

        val sideFooterDividerDrawable: Drawable?,
        val sideFooterDividerSize: Int?,
        val sideFooterDividerWidth: Int?,
        val sideFooterDividerHeight: Int?,
        val sideFooterDividerInsets: Int?,
        val sideFooterDividerInsetStart: Int?,
        val sideFooterDividerInsetTop: Int?,
        val sideFooterDividerInsetEnd: Int?,
        val sideFooterDividerInsetBottom: Int?,

        val useSideDividerAsSideHeaderDivider: Boolean?,
        val useSideDividerAsSideFooterDivider: Boolean?,
        val useSideDividerAsSideHeaderAndFooterDivider: Boolean?,
    ) {
        fun createLinearDividerItemDecoration(recyclerView: RecyclerView): LinearDividerItemDecoration? {
            if (dividerDrawable == null
                && headerDividerDrawable == null
                && footerDividerDrawable == null
                && sideHeaderDividerDrawable == null
                && sideFooterDividerDrawable == null
            ) {
                return null
            }

            return recyclerView.newLinearDividerItemDecoration {
                if (dividerDrawable != null) {
                    val dividerSize = if (dividerWidth != null && dividerHeight != null) {
                        DividerSize.clearly(dividerWidth, dividerHeight)
                    } else if (dividerSize != null) {
                        DividerSize.vague(dividerSize)
                    } else {
                        null
                    }
                    val dividerInsets = Insets.of(
                        dividerInsetStart ?: dividerInsets ?: 0,
                        dividerInsetTop ?: dividerInsets ?: 0,
                        dividerInsetEnd ?: dividerInsets ?: 0,
                        dividerInsetBottom ?: dividerInsets ?: 0
                    )
                    if (dividerSize != null) {
                        divider(
                            Divider.drawableWithSize(
                                dividerDrawable,
                                dividerSize,
                                dividerInsets
                            )
                        )
                    } else {
                        divider(Divider.drawable(dividerDrawable, insets = dividerInsets))
                    }
                }

                if (headerDividerDrawable != null) {
                    val headerDividerSize =
                        if (headerDividerWidth != null && headerDividerHeight != null) {
                            DividerSize.clearly(headerDividerWidth, headerDividerHeight)
                        } else if (headerDividerSize != null) {
                            DividerSize.vague(headerDividerSize)
                        } else {
                            null
                        }
                    val dividerInsets = Insets.of(
                        headerDividerInsetStart ?: headerDividerInsets ?: 0,
                        headerDividerInsetTop ?: headerDividerInsets ?: 0,
                        headerDividerInsetEnd ?: headerDividerInsets ?: 0,
                        headerDividerInsetBottom ?: headerDividerInsets ?: 0
                    )
                    if (headerDividerSize != null) {
                        headerDivider(
                            Divider.drawableWithSize(
                                headerDividerDrawable, headerDividerSize, dividerInsets
                            )
                        )
                    } else {
                        headerDivider(
                            Divider.drawable(headerDividerDrawable, insets = dividerInsets)
                        )
                    }
                }

                if (footerDividerDrawable != null) {
                    val footerDividerSize =
                        if (footerDividerWidth != null && footerDividerHeight != null) {
                            DividerSize.clearly(footerDividerWidth, footerDividerHeight)
                        } else if (footerDividerSize != null) {
                            DividerSize.vague(footerDividerSize)
                        } else {
                            null
                        }
                    val dividerInsets = Insets.of(
                        footerDividerInsetStart ?: footerDividerInsets ?: 0,
                        footerDividerInsetTop ?: footerDividerInsets ?: 0,
                        footerDividerInsetEnd ?: footerDividerInsets ?: 0,
                        footerDividerInsetBottom ?: footerDividerInsets ?: 0
                    )
                    if (footerDividerSize != null) {
                        footerDivider(
                            Divider.drawableWithSize(
                                footerDividerDrawable,
                                footerDividerSize,
                                dividerInsets
                            )
                        )
                    } else {
                        footerDivider(
                            Divider.drawable(
                                footerDividerDrawable,
                                insets = dividerInsets
                            )
                        )
                    }
                }

                if (useDividerAsHeaderDivider != null) {
                    useDividerAsHeaderDivider(useDividerAsHeaderDivider)
                }
                if (useDividerAsFooterDivider != null) {
                    useDividerAsFooterDivider(useDividerAsFooterDivider)
                }
                if (useDividerAsHeaderAndFooterDivider != null) {
                    useDividerAsHeaderAndFooterDivider(useDividerAsHeaderAndFooterDivider)
                }

                if (sideHeaderDividerDrawable != null) {
                    val sideHeaderDividerSize =
                        if (sideHeaderDividerWidth != null && sideHeaderDividerHeight != null) {
                            DividerSize.clearly(sideHeaderDividerWidth, sideHeaderDividerHeight)
                        } else if (sideHeaderDividerSize != null) {
                            DividerSize.vague(sideHeaderDividerSize)
                        } else {
                            null
                        }
                    val dividerInsets = Insets.of(
                        sideHeaderDividerInsetStart ?: sideHeaderDividerInsets ?: 0,
                        sideHeaderDividerInsetTop ?: sideHeaderDividerInsets ?: 0,
                        sideHeaderDividerInsetEnd ?: sideHeaderDividerInsets ?: 0,
                        sideHeaderDividerInsetBottom ?: sideHeaderDividerInsets ?: 0
                    )
                    if (sideHeaderDividerSize != null) {
                        sideHeaderDivider(
                            Divider.drawableWithSize(
                                sideHeaderDividerDrawable,
                                sideHeaderDividerSize,
                                dividerInsets
                            )
                        )
                    } else {
                        sideHeaderDivider(
                            Divider.drawable(sideHeaderDividerDrawable, insets = dividerInsets)
                        )
                    }
                }

                if (sideFooterDividerDrawable != null) {
                    val sideFooterDividerSize =
                        if (sideFooterDividerWidth != null && sideFooterDividerHeight != null) {
                            DividerSize.clearly(sideFooterDividerWidth, sideFooterDividerHeight)
                        } else if (sideFooterDividerSize != null) {
                            DividerSize.vague(sideFooterDividerSize)
                        } else {
                            null
                        }
                    val dividerInsets = Insets.of(
                        sideFooterDividerInsetStart ?: sideFooterDividerInsets ?: 0,
                        sideFooterDividerInsetTop ?: sideFooterDividerInsets ?: 0,
                        sideFooterDividerInsetEnd ?: sideFooterDividerInsets ?: 0,
                        sideFooterDividerInsetBottom ?: sideFooterDividerInsets ?: 0
                    )
                    if (sideFooterDividerSize != null) {
                        sideFooterDivider(
                            Divider.drawableWithSize(
                                sideFooterDividerDrawable,
                                sideFooterDividerSize,
                                dividerInsets
                            )
                        )
                    } else {
                        sideFooterDivider(
                            Divider.drawable(sideFooterDividerDrawable, insets = dividerInsets)
                        )
                    }
                }
            }
        }

        fun createGridDividerItemDecoration(recyclerView: RecyclerView): GridDividerItemDecoration? {
            if (dividerDrawable == null
                && headerDividerDrawable == null
                && footerDividerDrawable == null
                && sideDividerDrawable == null
                && sideHeaderDividerDrawable == null
                && sideFooterDividerDrawable == null
            ) {
                return null
            }

            return recyclerView.newGridDividerItemDecoration {
                if (dividerDrawable != null) {
                    val dividerSize = if (dividerWidth != null && dividerHeight != null) {
                        DividerSize.clearly(dividerWidth, dividerHeight)
                    } else if (dividerSize != null) {
                        DividerSize.vague(dividerSize)
                    } else {
                        null
                    }
                    val dividerInsets = Insets.of(
                        dividerInsetStart ?: dividerInsets ?: 0,
                        dividerInsetTop ?: dividerInsets ?: 0,
                        dividerInsetEnd ?: dividerInsets ?: 0,
                        dividerInsetBottom ?: dividerInsets ?: 0
                    )
                    if (dividerSize != null) {
                        divider(
                            Divider.drawableWithSize(
                                dividerDrawable,
                                dividerSize,
                                dividerInsets
                            )
                        )
                    } else {
                        divider(Divider.drawable(dividerDrawable, insets = dividerInsets))
                    }
                }

                if (headerDividerDrawable != null) {
                    val headerDividerSize =
                        if (headerDividerWidth != null && headerDividerHeight != null) {
                            DividerSize.clearly(headerDividerWidth, headerDividerHeight)
                        } else if (headerDividerSize != null) {
                            DividerSize.vague(headerDividerSize)
                        } else {
                            null
                        }
                    val dividerInsets = Insets.of(
                        headerDividerInsetStart ?: headerDividerInsets ?: 0,
                        headerDividerInsetTop ?: headerDividerInsets ?: 0,
                        headerDividerInsetEnd ?: headerDividerInsets ?: 0,
                        headerDividerInsetBottom ?: headerDividerInsets ?: 0
                    )
                    if (headerDividerSize != null) {
                        headerDivider(
                            Divider.drawableWithSize(
                                headerDividerDrawable,
                                headerDividerSize,
                                dividerInsets
                            )
                        )
                    } else {
                        headerDivider(
                            Divider.drawable(
                                headerDividerDrawable,
                                insets = dividerInsets
                            )
                        )
                    }
                }

                if (footerDividerDrawable != null) {
                    val footerDividerSize =
                        if (footerDividerWidth != null && footerDividerHeight != null) {
                            DividerSize.clearly(footerDividerWidth, footerDividerHeight)
                        } else if (footerDividerSize != null) {
                            DividerSize.vague(footerDividerSize)
                        } else {
                            null
                        }
                    val dividerInsets = Insets.of(
                        footerDividerInsetStart ?: footerDividerInsets ?: 0,
                        footerDividerInsetTop ?: footerDividerInsets ?: 0,
                        footerDividerInsetEnd ?: footerDividerInsets ?: 0,
                        footerDividerInsetBottom ?: footerDividerInsets ?: 0
                    )
                    if (footerDividerSize != null) {
                        footerDivider(
                            Divider.drawableWithSize(
                                footerDividerDrawable,
                                footerDividerSize,
                                dividerInsets
                            )
                        )
                    } else {
                        footerDivider(
                            Divider.drawable(
                                footerDividerDrawable,
                                insets = dividerInsets
                            )
                        )
                    }
                }

                if (useDividerAsHeaderDivider != null) {
                    useDividerAsHeaderDivider(useDividerAsHeaderDivider)
                }
                if (useDividerAsFooterDivider != null) {
                    useDividerAsFooterDivider(useDividerAsFooterDivider)
                }
                if (useDividerAsHeaderAndFooterDivider != null) {
                    useDividerAsHeaderAndFooterDivider(useDividerAsHeaderAndFooterDivider)
                }

                if (sideDividerDrawable != null) {
                    val sideDividerSize =
                        if (sideDividerWidth != null && sideDividerHeight != null) {
                            DividerSize.clearly(sideDividerWidth, sideDividerHeight)
                        } else if (sideDividerSize != null) {
                            DividerSize.vague(sideDividerSize)
                        } else {
                            null
                        }
                    val dividerInsets = Insets.of(
                        sideDividerInsetStart ?: sideDividerInsets ?: 0,
                        sideDividerInsetTop ?: sideDividerInsets ?: 0,
                        sideDividerInsetEnd ?: sideDividerInsets ?: 0,
                        sideDividerInsetBottom ?: sideDividerInsets ?: 0
                    )
                    if (sideDividerSize != null) {
                        sideDivider(
                            Divider.drawableWithSize(
                                sideDividerDrawable, sideDividerSize, dividerInsets
                            )
                        )
                    } else {
                        sideDivider(Divider.drawable(sideDividerDrawable, insets = dividerInsets))
                    }
                }

                if (sideHeaderDividerDrawable != null) {
                    val sideHeaderDividerSize =
                        if (sideHeaderDividerWidth != null && sideHeaderDividerHeight != null) {
                            DividerSize.clearly(sideHeaderDividerWidth, sideHeaderDividerHeight)
                        } else if (sideHeaderDividerSize != null) {
                            DividerSize.vague(sideHeaderDividerSize)
                        } else {
                            null
                        }
                    val dividerInsets = Insets.of(
                        sideHeaderDividerInsetStart ?: sideHeaderDividerInsets ?: 0,
                        sideHeaderDividerInsetTop ?: sideHeaderDividerInsets ?: 0,
                        sideHeaderDividerInsetEnd ?: sideHeaderDividerInsets ?: 0,
                        sideHeaderDividerInsetBottom ?: sideHeaderDividerInsets ?: 0
                    )
                    if (sideHeaderDividerSize != null) {
                        sideHeaderDivider(
                            Divider.drawableWithSize(
                                sideHeaderDividerDrawable,
                                sideHeaderDividerSize,
                                dividerInsets
                            )
                        )
                    } else {
                        sideHeaderDivider(
                            Divider.drawable(sideHeaderDividerDrawable, insets = dividerInsets)
                        )
                    }
                }

                if (sideFooterDividerDrawable != null) {
                    val sideFooterDividerSize =
                        if (sideFooterDividerWidth != null && sideFooterDividerHeight != null) {
                            DividerSize.clearly(sideFooterDividerWidth, sideFooterDividerHeight)
                        } else if (sideFooterDividerSize != null) {
                            DividerSize.vague(sideFooterDividerSize)
                        } else {
                            null
                        }
                    val dividerInsets = Insets.of(
                        sideFooterDividerInsetStart ?: sideFooterDividerInsets ?: 0,
                        sideFooterDividerInsetTop ?: sideFooterDividerInsets ?: 0,
                        sideFooterDividerInsetEnd ?: sideFooterDividerInsets ?: 0,
                        sideFooterDividerInsetBottom ?: sideFooterDividerInsets ?: 0
                    )
                    if (sideFooterDividerSize != null) {
                        sideFooterDivider(
                            Divider.drawableWithSize(
                                sideFooterDividerDrawable,
                                sideFooterDividerSize, dividerInsets
                            )
                        )
                    } else {
                        sideFooterDivider(
                            Divider.drawable(sideFooterDividerDrawable, insets = dividerInsets)
                        )
                    }
                }

                if (useSideDividerAsSideHeaderDivider != null) {
                    useSideDividerAsSideHeaderDivider(useSideDividerAsSideHeaderDivider)
                }
                if (useSideDividerAsSideFooterDivider != null) {
                    useSideDividerAsSideFooterDivider(useSideDividerAsSideFooterDivider)
                }
                if (useSideDividerAsSideHeaderAndFooterDivider != null) {
                    useSideDividerAsSideHeaderAndFooterDivider(
                        useSideDividerAsSideHeaderAndFooterDivider
                    )
                }
            }
        }

        fun createStaggeredGridDividerItemDecoration(
            recyclerView: RecyclerView,
            isFullSpanByPosition: IsFullSpanByPosition?
        ): StaggeredGridDividerItemDecoration? {
            if (dividerDrawable == null
                && headerDividerDrawable == null
                && footerDividerDrawable == null
                && sideDividerDrawable == null
                && sideHeaderDividerDrawable == null
                && sideFooterDividerDrawable == null
            ) {
                return null
            }

            return recyclerView.newStaggeredGridDividerItemDecoration {
                if (dividerDrawable != null) {
                    val dividerSize = if (dividerWidth != null && dividerHeight != null) {
                        DividerSize.clearly(dividerWidth, dividerHeight)
                    } else if (dividerSize != null) {
                        DividerSize.vague(dividerSize)
                    } else {
                        null
                    }
                    val dividerInsets = Insets.of(
                        dividerInsetStart ?: dividerInsets ?: 0,
                        dividerInsetTop ?: dividerInsets ?: 0,
                        dividerInsetEnd ?: dividerInsets ?: 0,
                        dividerInsetBottom ?: dividerInsets ?: 0
                    )
                    if (dividerSize != null) {
                        divider(
                            Divider.drawableWithSize(
                                dividerDrawable,
                                dividerSize,
                                dividerInsets
                            )
                        )
                    } else {
                        divider(Divider.drawable(dividerDrawable, insets = dividerInsets))
                    }
                }

                if (headerDividerDrawable != null) {
                    val headerDividerSize =
                        if (headerDividerWidth != null && headerDividerHeight != null) {
                            DividerSize.clearly(headerDividerWidth, headerDividerHeight)
                        } else if (headerDividerSize != null) {
                            DividerSize.vague(headerDividerSize)
                        } else {
                            null
                        }
                    val dividerInsets = Insets.of(
                        headerDividerInsetStart ?: headerDividerInsets ?: 0,
                        headerDividerInsetTop ?: headerDividerInsets ?: 0,
                        headerDividerInsetEnd ?: headerDividerInsets ?: 0,
                        headerDividerInsetBottom ?: headerDividerInsets ?: 0
                    )
                    if (headerDividerSize != null) {
                        headerDivider(
                            Divider.drawableWithSize(
                                headerDividerDrawable,
                                headerDividerSize,
                                dividerInsets
                            )
                        )
                    } else {
                        headerDivider(
                            Divider.drawable(
                                headerDividerDrawable,
                                insets = dividerInsets
                            )
                        )
                    }
                }

                if (footerDividerDrawable != null) {
                    val footerDividerSize =
                        if (footerDividerWidth != null && footerDividerHeight != null) {
                            DividerSize.clearly(footerDividerWidth, footerDividerHeight)
                        } else if (footerDividerSize != null) {
                            DividerSize.vague(footerDividerSize)
                        } else {
                            null
                        }
                    val dividerInsets = Insets.of(
                        footerDividerInsetStart ?: footerDividerInsets ?: 0,
                        footerDividerInsetTop ?: footerDividerInsets ?: 0,
                        footerDividerInsetEnd ?: footerDividerInsets ?: 0,
                        footerDividerInsetBottom ?: footerDividerInsets ?: 0
                    )
                    if (footerDividerSize != null) {
                        footerDivider(
                            Divider.drawableWithSize(
                                footerDividerDrawable,
                                footerDividerSize,
                                dividerInsets
                            )
                        )
                    } else {
                        footerDivider(
                            Divider.drawable(
                                footerDividerDrawable,
                                insets = dividerInsets
                            )
                        )
                    }
                }

                if (useDividerAsHeaderDivider != null) {
                    useDividerAsHeaderDivider(useDividerAsHeaderDivider)
                }
                if (useDividerAsFooterDivider != null) {
                    useDividerAsFooterDivider(useDividerAsFooterDivider)
                }
                if (useDividerAsHeaderAndFooterDivider != null) {
                    useDividerAsHeaderAndFooterDivider(useDividerAsHeaderAndFooterDivider)
                }

                if (sideDividerDrawable != null) {
                    val sideDividerSize =
                        if (sideDividerWidth != null && sideDividerHeight != null) {
                            DividerSize.clearly(sideDividerWidth, sideDividerHeight)
                        } else if (sideDividerSize != null) {
                            DividerSize.vague(sideDividerSize)
                        } else {
                            null
                        }
                    val dividerInsets = Insets.of(
                        sideDividerInsetStart ?: sideDividerInsets ?: 0,
                        sideDividerInsetTop ?: sideDividerInsets ?: 0,
                        sideDividerInsetEnd ?: sideDividerInsets ?: 0,
                        sideDividerInsetBottom ?: sideDividerInsets ?: 0
                    )
                    if (sideDividerSize != null) {
                        sideDivider(
                            Divider.drawableWithSize(
                                sideDividerDrawable,
                                sideDividerSize,
                                dividerInsets
                            )
                        )
                    } else {
                        sideDivider(Divider.drawable(sideDividerDrawable, insets = dividerInsets))
                    }
                }

                if (sideHeaderDividerDrawable != null) {
                    val sideHeaderDividerSize =
                        if (sideHeaderDividerWidth != null && sideHeaderDividerHeight != null) {
                            DividerSize.clearly(sideHeaderDividerWidth, sideHeaderDividerHeight)
                        } else if (sideHeaderDividerSize != null) {
                            DividerSize.vague(sideHeaderDividerSize)
                        } else {
                            null
                        }
                    val dividerInsets = Insets.of(
                        sideHeaderDividerInsetStart ?: sideHeaderDividerInsets ?: 0,
                        sideHeaderDividerInsetTop ?: sideHeaderDividerInsets ?: 0,
                        sideHeaderDividerInsetEnd ?: sideHeaderDividerInsets ?: 0,
                        sideHeaderDividerInsetBottom ?: sideHeaderDividerInsets ?: 0
                    )
                    if (sideHeaderDividerSize != null) {
                        sideHeaderDivider(
                            Divider.drawableWithSize(
                                sideHeaderDividerDrawable,
                                sideHeaderDividerSize, dividerInsets
                            )
                        )
                    } else {
                        sideHeaderDivider(
                            Divider.drawable(sideHeaderDividerDrawable, insets = dividerInsets)
                        )
                    }
                }

                if (sideFooterDividerDrawable != null) {
                    val sideFooterDividerSize =
                        if (sideFooterDividerWidth != null && sideFooterDividerHeight != null) {
                            DividerSize.clearly(sideFooterDividerWidth, sideFooterDividerHeight)
                        } else if (sideFooterDividerSize != null) {
                            DividerSize.vague(sideFooterDividerSize)
                        } else {
                            null
                        }
                    val dividerInsets = Insets.of(
                        sideFooterDividerInsetStart ?: sideFooterDividerInsets ?: 0,
                        sideFooterDividerInsetTop ?: sideFooterDividerInsets ?: 0,
                        sideFooterDividerInsetEnd ?: sideFooterDividerInsets ?: 0,
                        sideFooterDividerInsetBottom ?: sideFooterDividerInsets ?: 0
                    )
                    if (sideFooterDividerSize != null) {
                        sideFooterDivider(
                            Divider.drawableWithSize(
                                sideFooterDividerDrawable,
                                sideFooterDividerSize, dividerInsets
                            )
                        )
                    } else {
                        sideFooterDivider(
                            Divider.drawable(sideFooterDividerDrawable, insets = dividerInsets)
                        )
                    }
                }

                if (useSideDividerAsSideHeaderDivider != null) {
                    useSideDividerAsSideHeaderDivider(useSideDividerAsSideHeaderDivider)
                }
                if (useSideDividerAsSideFooterDivider != null) {
                    useSideDividerAsSideFooterDivider(useSideDividerAsSideFooterDivider)
                }
                if (useSideDividerAsSideHeaderAndFooterDivider != null) {
                    useSideDividerAsSideHeaderAndFooterDivider(
                        useSideDividerAsSideHeaderAndFooterDivider
                    )
                }

                isFullSpanByPosition(isFullSpanByPosition)
            }
        }

        companion object {
            fun fromAttrs(typedArray: TypedArray): DividerParams = DividerParams(
                dividerDrawable = typedArray.getDrawable(R.styleable.DividerRecyclerView_drv_divider),
                dividerSize = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_dividerSize, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                dividerWidth = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_dividerWidth, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                dividerHeight = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_dividerHeight, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                dividerInsets = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_dividerInsets, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                dividerInsetStart = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_dividerInsetStart, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                dividerInsetTop = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_dividerInsetTop, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                dividerInsetEnd = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_dividerInsetEnd, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                dividerInsetBottom = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_dividerInsetBottom, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },

                headerDividerDrawable =
                typedArray.getDrawable(R.styleable.DividerRecyclerView_drv_headerDivider),
                headerDividerSize = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_headerDividerSize, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                headerDividerWidth = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_headerDividerWidth, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                headerDividerHeight = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_headerDividerHeight, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                headerDividerInsets = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_headerDividerInsets, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                headerDividerInsetStart = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_headerDividerInsetStart, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                headerDividerInsetTop = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_headerDividerInsetTop, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                headerDividerInsetEnd = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_headerDividerInsetEnd, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                headerDividerInsetBottom = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_headerDividerInsetBottom, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },

                footerDividerDrawable =
                typedArray.getDrawable(R.styleable.DividerRecyclerView_drv_footerDivider),
                footerDividerSize = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_footerDividerSize, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                footerDividerWidth = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_footerDividerWidth, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                footerDividerHeight = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_footerDividerHeight, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                footerDividerInsets = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_footerDividerInsets, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                footerDividerInsetStart = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_footerDividerInsetStart, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                footerDividerInsetTop = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_footerDividerInsetTop, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                footerDividerInsetEnd = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_footerDividerInsetEnd, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                footerDividerInsetBottom = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_footerDividerInsetBottom, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },

                useDividerAsHeaderDivider = typedArray.getBoolean(
                    R.styleable.DividerRecyclerView_drv_useDividerAsHeaderDivider, false
                ).takeIf {
                    it || (!it && !typedArray.getBoolean(
                        R.styleable.DividerRecyclerView_drv_useDividerAsHeaderDivider, true
                    ))
                },
                useDividerAsFooterDivider = typedArray.getBoolean(
                    R.styleable.DividerRecyclerView_drv_useDividerAsFooterDivider, false
                ).takeIf {
                    it || (!it && !typedArray.getBoolean(
                        R.styleable.DividerRecyclerView_drv_useDividerAsFooterDivider, true
                    ))
                },
                useDividerAsHeaderAndFooterDivider = typedArray.getBoolean(
                    R.styleable.DividerRecyclerView_drv_useDividerAsHeaderAndFooterDivider, false
                ).takeIf {
                    it || (!it && !typedArray.getBoolean(
                        R.styleable.DividerRecyclerView_drv_useDividerAsHeaderAndFooterDivider, true
                    ))
                },

                sideDividerDrawable =
                typedArray.getDrawable(R.styleable.DividerRecyclerView_drv_sideDivider),
                sideDividerSize = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_sideDividerSize, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                sideDividerWidth = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_sideDividerWidth, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                sideDividerHeight = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_sideDividerHeight, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                sideDividerInsets = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_sideDividerInsets, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                sideDividerInsetStart = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_sideDividerInsetStart, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                sideDividerInsetTop = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_sideDividerInsetTop, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                sideDividerInsetEnd = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_sideDividerInsetEnd, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                sideDividerInsetBottom = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_sideDividerInsetBottom, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },

                sideHeaderDividerDrawable =
                typedArray.getDrawable(R.styleable.DividerRecyclerView_drv_sideHeaderDivider),
                sideHeaderDividerSize = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_sideHeaderDividerSize, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                sideHeaderDividerWidth = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_sideHeaderDividerWidth, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                sideHeaderDividerHeight = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_sideHeaderDividerHeight, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                sideHeaderDividerInsets = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_sideHeaderDividerInsets, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                sideHeaderDividerInsetStart = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_sideHeaderDividerInsetStart, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                sideHeaderDividerInsetTop = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_sideHeaderDividerInsetTop, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                sideHeaderDividerInsetEnd = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_sideHeaderDividerInsetEnd, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                sideHeaderDividerInsetBottom = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_sideHeaderDividerInsetBottom, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },

                sideFooterDividerDrawable =
                typedArray.getDrawable(R.styleable.DividerRecyclerView_drv_sideFooterDivider),
                sideFooterDividerSize = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_sideFooterDividerSize, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                sideFooterDividerWidth = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_sideFooterDividerWidth, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                sideFooterDividerHeight = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_sideFooterDividerHeight, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                sideFooterDividerInsets = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_sideFooterDividerInsets, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                sideFooterDividerInsetStart = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_sideFooterDividerInsetStart, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                sideFooterDividerInsetTop = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_sideFooterDividerInsetTop, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                sideFooterDividerInsetEnd = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_sideFooterDividerInsetEnd, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },
                sideFooterDividerInsetBottom = typedArray.getDimensionPixelSize(
                    R.styleable.DividerRecyclerView_drv_sideFooterDividerInsetBottom, Int.MIN_VALUE
                ).takeIf { it != Int.MIN_VALUE },

                useSideDividerAsSideHeaderDivider = typedArray.getBoolean(
                    R.styleable.DividerRecyclerView_drv_useSideDividerAsSideHeaderDivider, false
                ).takeIf {
                    it || (!it && !typedArray.getBoolean(
                        R.styleable.DividerRecyclerView_drv_useSideDividerAsSideHeaderDivider, true
                    ))
                },
                useSideDividerAsSideFooterDivider = typedArray.getBoolean(
                    R.styleable.DividerRecyclerView_drv_useSideDividerAsSideFooterDivider, false
                ).takeIf {
                    it || (!it && !typedArray.getBoolean(
                        R.styleable.DividerRecyclerView_drv_useSideDividerAsSideFooterDivider, true
                    ))
                },
                useSideDividerAsSideHeaderAndFooterDivider = typedArray.getBoolean(
                    R.styleable.DividerRecyclerView_drv_useSideDividerAsSideHeaderAndFooterDivider,
                    false
                ).takeIf {
                    it || (!it && !typedArray.getBoolean(
                        R.styleable.DividerRecyclerView_drv_useSideDividerAsSideHeaderAndFooterDivider,
                        true
                    ))
                },
            )
        }
    }

    private class ItemDecorationWrapper(private val itemDecoration: ItemDecoration) : ItemDecoration() {
        override fun onDraw(c: Canvas, parent: RecyclerView, state: State) {
            itemDecoration.onDraw(c, parent, state)
        }

        override fun onDraw(c: Canvas, parent: RecyclerView) {
            itemDecoration.onDraw(c, parent)
        }

        override fun onDrawOver(c: Canvas, parent: RecyclerView, state: State) {
            itemDecoration.onDrawOver(c, parent, state)
        }

        override fun onDrawOver(c: Canvas, parent: RecyclerView) {
            itemDecoration.onDrawOver(c, parent)
        }

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
            itemDecoration.getItemOffsets(outRect, view, parent, state)
        }

        override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
            itemDecoration.getItemOffsets(outRect, itemPosition, parent)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as ItemDecorationWrapper
            if (itemDecoration != other.itemDecoration) return false
            return true
        }

        override fun hashCode(): Int {
            return itemDecoration.hashCode()
        }

        override fun toString(): String {
            return "ItemDecorationWrapper(itemDecoration=$itemDecoration)"
        }
    }
}