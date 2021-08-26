# new
* fix: Fix the bug of AbsoluteAdapterPosition error returned by ConcatAdapterAbsoluteHelper 
* fix: ConcatAdapterLocalHelper now throws IndexOutOfBoundsException when position is out of range
* change: AnyAdapterDataObserver change to SimpleAdapterDataObserver
* fix: Fix the bug that the Insets.topAndBottomOf() method reverses start and top
* change: The position priority of DividerConfig is now higher than spanIndex
* fix: Fix the bug that GridDividerItemDecoration encounters an item with a spanSize greater than 1 and less than spanCount that its isLastSpan is calculated incorrectly


# v4.0.0-beta01

全新版本，全新出发，4.0 版本 和 3.\* 版本完全不兼容，但两者可以共存

新版本使用方法请参考 [README.md](README.md)

同时提供了使用 4.0 API 实现的兼容 3.\* API 的一些类，请参考 [使用新版 4.* API 兼容旧版 3.* API](docs/wiki/old_api_compat.md)