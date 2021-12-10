package com.github.panpf.assemblyadapter.recycler.divider

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.panpf.assemblyadapter.common.recycler.divider.R
import com.github.panpf.assemblyadapter.recycler.divider.internal.DividerSize

class DividerRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.attr.recyclerViewStyle
) : RecyclerView(context, attrs, defStyleAttr) {

    private var dividerParams: DividerParams? = null
    // todo setting IsFullSpanByPosition for staggered

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DividerRecyclerView)
        dividerParams = DividerParams.fromAttrs(typedArray)
        typedArray.recycle()
        if (layoutManager == null && isInEditMode) {
            super.setLayoutManager(LinearLayoutManager(context))
        }
        resetDivider(layoutManager)
    }

    override fun setLayoutManager(layout: LayoutManager?) {
        super.setLayoutManager(layout)
        resetDivider(layout)
    }

    private fun resetDivider(layout: LayoutManager?) {
        val existDividerItemDecorationWrapperIndex = (0 until itemDecorationCount).find {
            when (getItemDecorationAt(it)) {
                is LinearDividerItemDecorationWrapper -> true
                is GridDividerItemDecorationWrapper -> true
                is StaggeredGridDividerItemDecorationWrapper -> true
                else -> false
            }
        }
        if (existDividerItemDecorationWrapperIndex != null) {
            removeItemDecorationAt(existDividerItemDecorationWrapperIndex)
        }

        val dividerParams = dividerParams
        if (dividerParams != null) {
            val dividerItemDecoration = when (layout) {
                is LinearLayoutManager -> {
                    dividerParams.createLinearDividerItemDecoration(this)?.let {
                        LinearDividerItemDecorationWrapper(it)
                    }
                }
                is GridLayoutManager -> {
                    dividerParams.createGridDividerItemDecoration(this)?.let {
                        GridDividerItemDecorationWrapper(it)
                    }
                }
                is StaggeredGridLayoutManager -> {
                    dividerParams.createStaggeredGridDividerItemDecoration(this)?.let {
                        StaggeredGridDividerItemDecorationWrapper(it)
                    }
                }
                else -> null
            }
            if (dividerItemDecoration != null) {
                addItemDecoration(dividerItemDecoration)
            }
        }
    }

    private class DividerParams(
        val dividerDrawable: Drawable?,
        val dividerSize: Int?,
        val dividerWidth: Int?,
        val dividerHeight: Int?,

        val headerDividerDrawable: Drawable?,
        val headerDividerSize: Int?,
        val headerDividerWidth: Int?,
        val headerDividerHeight: Int?,

        val footerDividerDrawable: Drawable?,
        val footerDividerSize: Int?,
        val footerDividerWidth: Int?,
        val footerDividerHeight: Int?,

        val useDividerAsHeaderDivider: Boolean?,
        val useDividerAsFooterDivider: Boolean?,
        val useDividerAsHeaderAndFooterDivider: Boolean?,

        val sideDividerDrawable: Drawable?,
        val sideDividerSize: Int?,
        val sideDividerWidth: Int?,
        val sideDividerHeight: Int?,

        val sideHeaderDividerDrawable: Drawable?,
        val sideHeaderDividerSize: Int?,
        val sideHeaderDividerWidth: Int?,
        val sideHeaderDividerHeight: Int?,

        val sideFooterDividerDrawable: Drawable?,
        val sideFooterDividerSize: Int?,
        val sideFooterDividerWidth: Int?,
        val sideFooterDividerHeight: Int?,

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
                    if (dividerSize != null) {
                        divider(Divider.drawableWithSize(dividerDrawable, dividerSize))
                    } else {
                        divider(Divider.drawable(dividerDrawable))
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
                    if (headerDividerSize != null) {
                        headerDivider(
                            Divider.drawableWithSize(headerDividerDrawable, headerDividerSize)
                        )
                    } else {
                        headerDivider(Divider.drawable(headerDividerDrawable))
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
                    if (footerDividerSize != null) {
                        footerDivider(
                            Divider.drawableWithSize(footerDividerDrawable, footerDividerSize)
                        )
                    } else {
                        footerDivider(Divider.drawable(footerDividerDrawable))
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
                    if (sideHeaderDividerSize != null) {
                        sideHeaderDivider(
                            Divider.drawableWithSize(
                                sideHeaderDividerDrawable,
                                sideHeaderDividerSize
                            )
                        )
                    } else {
                        sideHeaderDivider(Divider.drawable(sideHeaderDividerDrawable))
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
                    if (sideFooterDividerSize != null) {
                        sideFooterDivider(
                            Divider.drawableWithSize(
                                sideFooterDividerDrawable,
                                sideFooterDividerSize
                            )
                        )
                    } else {
                        sideFooterDivider(Divider.drawable(sideFooterDividerDrawable))
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
                    if (dividerSize != null) {
                        divider(Divider.drawableWithSize(dividerDrawable, dividerSize))
                    } else {
                        divider(Divider.drawable(dividerDrawable))
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
                    if (headerDividerSize != null) {
                        headerDivider(
                            Divider.drawableWithSize(headerDividerDrawable, headerDividerSize)
                        )
                    } else {
                        headerDivider(Divider.drawable(headerDividerDrawable))
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
                    if (footerDividerSize != null) {
                        footerDivider(
                            Divider.drawableWithSize(footerDividerDrawable, footerDividerSize)
                        )
                    } else {
                        footerDivider(Divider.drawable(footerDividerDrawable))
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
                    if (sideDividerSize != null) {
                        sideDivider(Divider.drawableWithSize(sideDividerDrawable, sideDividerSize))
                    } else {
                        sideDivider(Divider.drawable(sideDividerDrawable))
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
                    if (sideHeaderDividerSize != null) {
                        sideHeaderDivider(
                            Divider.drawableWithSize(
                                sideHeaderDividerDrawable,
                                sideHeaderDividerSize
                            )
                        )
                    } else {
                        sideHeaderDivider(Divider.drawable(sideHeaderDividerDrawable))
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
                    if (sideFooterDividerSize != null) {
                        sideFooterDivider(
                            Divider.drawableWithSize(
                                sideFooterDividerDrawable,
                                sideFooterDividerSize
                            )
                        )
                    } else {
                        sideFooterDivider(Divider.drawable(sideFooterDividerDrawable))
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

        fun createStaggeredGridDividerItemDecoration(recyclerView: RecyclerView): StaggeredGridDividerItemDecoration? {
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
                    if (dividerSize != null) {
                        divider(Divider.drawableWithSize(dividerDrawable, dividerSize))
                    } else {
                        divider(Divider.drawable(dividerDrawable))
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
                    if (headerDividerSize != null) {
                        headerDivider(
                            Divider.drawableWithSize(headerDividerDrawable, headerDividerSize)
                        )
                    } else {
                        headerDivider(Divider.drawable(headerDividerDrawable))
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
                    if (footerDividerSize != null) {
                        footerDivider(
                            Divider.drawableWithSize(footerDividerDrawable, footerDividerSize)
                        )
                    } else {
                        footerDivider(Divider.drawable(footerDividerDrawable))
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
                    if (sideDividerSize != null) {
                        sideDivider(Divider.drawableWithSize(sideDividerDrawable, sideDividerSize))
                    } else {
                        sideDivider(Divider.drawable(sideDividerDrawable))
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
                    if (sideHeaderDividerSize != null) {
                        sideHeaderDivider(
                            Divider.drawableWithSize(
                                sideHeaderDividerDrawable,
                                sideHeaderDividerSize
                            )
                        )
                    } else {
                        sideHeaderDivider(Divider.drawable(sideHeaderDividerDrawable))
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
                    if (sideFooterDividerSize != null) {
                        sideFooterDivider(
                            Divider.drawableWithSize(
                                sideFooterDividerDrawable,
                                sideFooterDividerSize
                            )
                        )
                    } else {
                        sideFooterDivider(Divider.drawable(sideFooterDividerDrawable))
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

    class LinearDividerItemDecorationWrapper(
        itemDecoration: LinearDividerItemDecoration
    ) : LinearDividerItemDecoration(
        itemDecoration.dividerConfig,
        itemDecoration.headerDividerConfig,
        itemDecoration.footerDividerConfig,
        itemDecoration.sideHeaderDividerConfig,
        itemDecoration.sideFooterDividerConfig
    )

    class GridDividerItemDecorationWrapper(
        itemDecoration: GridDividerItemDecoration
    ) : GridDividerItemDecoration(
        itemDecoration.dividerConfig,
        itemDecoration.headerDividerConfig,
        itemDecoration.footerDividerConfig,
        itemDecoration.sideDividerConfig,
        itemDecoration.sideHeaderDividerConfig,
        itemDecoration.sideFooterDividerConfig
    )

    class StaggeredGridDividerItemDecorationWrapper(
        itemDecoration: StaggeredGridDividerItemDecoration
    ) : StaggeredGridDividerItemDecoration(
        itemDecoration.dividerConfig,
        itemDecoration.headerDividerConfig,
        itemDecoration.footerDividerConfig,
        itemDecoration.sideDividerConfig,
        itemDecoration.sideHeaderDividerConfig,
        itemDecoration.sideFooterDividerConfig,
        itemDecoration.isFullSpanByPosition
    )
}