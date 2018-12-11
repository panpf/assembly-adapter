@file:Suppress("unused")

package me.panpf.args.ktx

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import androidx.annotation.RequiresApi
import android.util.Size
import android.util.SizeF
import android.util.SparseArray
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import android.app.Fragment as OriginFragment
import androidx.fragment.app.Fragment as SupportFragment

/* ************************************* OriginFragment ***************************************** */

// Boolean
fun OriginFragment.bindBooleanArg(argName: String, defaultValue: Boolean = false): ReadOnlyProperty<OriginFragment, Boolean> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getBoolean(argName, defaultValue) }

fun OriginFragment.bindOptionalBooleanArg(argName: String, defaultValue: Boolean = false): ReadOnlyProperty<OriginFragment, Boolean?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getBoolean(argName, defaultValue) }

// BooleanArray
fun OriginFragment.bindBooleanArrayArg(argName: String): ReadOnlyProperty<OriginFragment, BooleanArray> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getBooleanArray(argName) }

fun OriginFragment.bindOptionalBooleanArrayArg(argName: String): ReadOnlyProperty<OriginFragment, BooleanArray?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getBooleanArray(argName) }


// Byte
fun OriginFragment.bindByteArg(argName: String, defaultValue: Byte = 0): ReadOnlyProperty<OriginFragment, Byte> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getByte(argName, defaultValue) }

fun OriginFragment.bindOptionalByteArg(argName: String, defaultValue: Byte = 0): ReadOnlyProperty<OriginFragment, Byte?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getByte(argName, defaultValue) }

// ByteArray
fun OriginFragment.bindByteArrayArg(argName: String): ReadOnlyProperty<OriginFragment, ByteArray> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getByteArray(argName) }

fun OriginFragment.bindOptionalByteArrayArg(argName: String): ReadOnlyProperty<OriginFragment, ByteArray?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getByteArray(argName) }


// Char
fun OriginFragment.bindCharArg(argName: String, defaultValue: Char = 0.toChar()): ReadOnlyProperty<OriginFragment, Char> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getChar(argName, defaultValue) }

fun OriginFragment.bindOptionalCharArg(argName: String, defaultValue: Char = 0.toChar()): ReadOnlyProperty<OriginFragment, Char?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getChar(argName, defaultValue) }

// CharArray
fun OriginFragment.bindCharArrayArg(argName: String): ReadOnlyProperty<OriginFragment, CharArray> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getCharArray(argName) }

fun OriginFragment.bindOptionalCharArrayArg(argName: String): ReadOnlyProperty<OriginFragment, CharArray?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getCharArray(argName) }


// Short
fun OriginFragment.bindShortArg(argName: String, defaultValue: Short = 0): ReadOnlyProperty<OriginFragment, Short> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getShort(argName, defaultValue) }

fun OriginFragment.bindOptionalShortArg(argName: String, defaultValue: Short = 0): ReadOnlyProperty<OriginFragment, Short?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getShort(argName, defaultValue) }

// ShortArray
fun OriginFragment.bindShortArrayArg(argName: String): ReadOnlyProperty<OriginFragment, ShortArray> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getShortArray(argName) }

fun OriginFragment.bindOptionalShortArrayArg(argName: String): ReadOnlyProperty<OriginFragment, ShortArray?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getShortArray(argName) }


// Float
fun OriginFragment.bindFloatArg(argName: String, defaultValue: Float = 0f): ReadOnlyProperty<OriginFragment, Float> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getFloat(argName, defaultValue) }

fun OriginFragment.bindOptionalFloatArg(argName: String, defaultValue: Float = 0f): ReadOnlyProperty<OriginFragment, Float?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getFloat(argName, defaultValue) }

// FloatArray
fun OriginFragment.bindFloatArrayArg(argName: String): ReadOnlyProperty<OriginFragment, FloatArray> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getFloatArray(argName) }

fun OriginFragment.bindOptionalFloatArrayArg(argName: String): ReadOnlyProperty<OriginFragment, FloatArray?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getFloatArray(argName) }


// Int
fun OriginFragment.bindIntArg(argName: String, defaultValue: Int = 0): ReadOnlyProperty<OriginFragment, Int> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getInt(argName, defaultValue) }

fun OriginFragment.bindOptionalIntArg(argName: String, defaultValue: Int = 0): ReadOnlyProperty<OriginFragment, Int?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getInt(argName, defaultValue) }

// IntArray
fun OriginFragment.bindIntArrayArg(argName: String): ReadOnlyProperty<OriginFragment, IntArray> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getIntArray(argName) }

fun OriginFragment.bindOptionalIntArrayArg(argName: String): ReadOnlyProperty<OriginFragment, IntArray?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getIntArray(argName) }

// ArrayList<Int>
fun OriginFragment.bindIntArrayListArg(argName: String): ReadOnlyProperty<OriginFragment, ArrayList<Int>> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getIntegerArrayList(argName) }

fun OriginFragment.bindOptionalIntArrayListArg(argName: String): ReadOnlyProperty<OriginFragment, ArrayList<Int>?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getIntegerArrayList(argName) }


// Double
fun OriginFragment.bindDoubleArg(argName: String, defaultValue: Double = 0.toDouble()): ReadOnlyProperty<OriginFragment, Double> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getDouble(argName, defaultValue) }

fun OriginFragment.bindOptionalDoubleArg(argName: String, defaultValue: Double = 0.toDouble()): ReadOnlyProperty<OriginFragment, Double?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getDouble(argName, defaultValue) }

// DoubleArray
fun OriginFragment.bindDoubleArrayArg(argName: String): ReadOnlyProperty<OriginFragment, DoubleArray> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getDoubleArray(argName) }

fun OriginFragment.bindOptionalDoubleArrayArg(argName: String): ReadOnlyProperty<OriginFragment, DoubleArray?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getDoubleArray(argName) }


// Long
fun OriginFragment.bindLongArg(argName: String, defaultValue: Long = 0L): ReadOnlyProperty<OriginFragment, Long> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getLong(argName, defaultValue) }

fun OriginFragment.bindOptionalLongArg(argName: String, defaultValue: Long = 0L): ReadOnlyProperty<OriginFragment, Long?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getLong(argName, defaultValue) }

// LongArray
fun OriginFragment.bindLongArrayArg(argName: String): ReadOnlyProperty<OriginFragment, LongArray> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getLongArray(argName) }

fun OriginFragment.bindOptionalLongArrayArg(argName: String): ReadOnlyProperty<OriginFragment, LongArray?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getLongArray(argName) }


// CharSequence
fun OriginFragment.bindCharSequenceArg(argName: String, defaultValue: CharSequence? = null): ReadOnlyProperty<OriginFragment, CharSequence> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getCharSequence(argName, defaultValue) }

fun OriginFragment.bindOptionalCharSequenceArg(argName: String, defaultValue: CharSequence? = null): ReadOnlyProperty<OriginFragment, CharSequence?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getCharSequence(argName, defaultValue) }

// Array<CharSequence>
fun OriginFragment.bindCharSequenceArrayArg(argName: String): ReadOnlyProperty<OriginFragment, Array<CharSequence>> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getCharSequenceArray(argName) }

fun OriginFragment.bindOptionalCharSequenceArrayArg(argName: String): ReadOnlyProperty<OriginFragment, Array<CharSequence>?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getCharSequenceArray(argName) }


// String
fun OriginFragment.bindStringArg(argName: String): ReadOnlyProperty<OriginFragment, String> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getString(argName) }

fun OriginFragment.bindOptionalStringArg(argName: String): ReadOnlyProperty<OriginFragment, String?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getString(argName) }

// Array<String>
fun OriginFragment.bindStringArrayArg(argName: String): ReadOnlyProperty<OriginFragment, Array<String>> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getStringArray(argName) }

fun OriginFragment.bindOptionalStringArrayArg(argName: String): ReadOnlyProperty<OriginFragment, Array<String>?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getStringArray(argName) }

// ArrayList<String>
fun OriginFragment.bindStringArrayListArg(argName: String): ReadOnlyProperty<OriginFragment, ArrayList<String>> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getStringArrayList(argName) }

fun OriginFragment.bindOptionalStringArrayListArg(argName: String): ReadOnlyProperty<OriginFragment, ArrayList<String>?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getStringArrayList(argName) }


// Parcelable
fun <V : Parcelable> OriginFragment.bindParcelableArg(argName: String): ReadOnlyProperty<OriginFragment, V> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getParcelable(argName) }

fun <V : Parcelable> OriginFragment.bindOptionalParcelableArg(argName: String): ReadOnlyProperty<OriginFragment, V?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getParcelable(argName) }

// Array<Parcelable>
@Suppress("UNCHECKED_CAST")
fun <V : Parcelable> OriginFragment.bindParcelableArrayArg(argName: String): ReadOnlyProperty<OriginFragment, Array<V>> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getParcelableArray(argName) as Array<V> }

@Suppress("UNCHECKED_CAST")
fun <V : Parcelable> OriginFragment.bindOptionalParcelableArrayArg(argName: String): ReadOnlyProperty<OriginFragment, Array<V>?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getParcelableArray(argName) as Array<V>? }

// ArrayList<Parcelable>
fun <V : Parcelable> OriginFragment.bindParcelableArrayListArg(argName: String): ReadOnlyProperty<OriginFragment, ArrayList<V>> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getParcelableArrayList(argName) }

fun <V : Parcelable> OriginFragment.bindOptionalParcelableArrayListArg(argName: String): ReadOnlyProperty<OriginFragment, ArrayList<V>?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getParcelableArrayList(argName) }

// SparseArray<Parcelable>
fun <V : Parcelable> OriginFragment.bindSparseParcelableArrayArg(argName: String): ReadOnlyProperty<OriginFragment, SparseArray<V>> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getSparseParcelableArray(argName) }

fun <V : Parcelable> OriginFragment.bindOptionalSparseParcelableArrayArg(argName: String): ReadOnlyProperty<OriginFragment, SparseArray<V>?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getSparseParcelableArray(argName) }

// Serializable
@Suppress("UNCHECKED_CAST")
fun <V : java.io.Serializable> OriginFragment.bindSerializableArg(argName: String): ReadOnlyProperty<OriginFragment, V> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getSerializable(argName) as V }

@Suppress("UNCHECKED_CAST")
fun <V : java.io.Serializable> OriginFragment.bindOptionalSerializableArg(argName: String): ReadOnlyProperty<OriginFragment, V?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getSerializable(argName) as V? }


// IBinder
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
fun OriginFragment.bindBinderArg(argName: String): ReadOnlyProperty<OriginFragment, IBinder> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getBinder(argName) }

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
fun OriginFragment.bindOptionalBinderArg(argName: String): ReadOnlyProperty<OriginFragment, IBinder?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getBinder(argName) }


// Bundle
fun OriginFragment.bindBundleArg(argName: String): ReadOnlyProperty<OriginFragment, Bundle> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getBundle(argName) }

fun OriginFragment.bindOptionalBundleArg(argName: String): ReadOnlyProperty<OriginFragment, Bundle?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getBundle(argName) }


// Size
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun OriginFragment.bindSizeArg(argName: String): ReadOnlyProperty<OriginFragment, Size> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getSize(argName) }

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun OriginFragment.bindOptionalSizeArg(argName: String): ReadOnlyProperty<OriginFragment, Size?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getSize(argName) }


// SizeF
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun OriginFragment.bindSizeFArg(argName: String): ReadOnlyProperty<OriginFragment, SizeF> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getSizeF(argName) }

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun OriginFragment.bindOptionalSizeFArg(argName: String): ReadOnlyProperty<OriginFragment, SizeF?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getSizeF(argName) }


/* ************************************* SupportFragment ***************************************** */

// Boolean
fun SupportFragment.bindBooleanArg(argName: String, defaultValue: Boolean = false): ReadOnlyProperty<SupportFragment, Boolean> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getBoolean(argName, defaultValue) }

fun SupportFragment.bindOptionalBooleanArg(argName: String, defaultValue: Boolean = false): ReadOnlyProperty<SupportFragment, Boolean?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getBoolean(argName, defaultValue) }

// BooleanArray
fun SupportFragment.bindBooleanArrayArg(argName: String): ReadOnlyProperty<SupportFragment, BooleanArray> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getBooleanArray(argName) }

fun SupportFragment.bindOptionalBooleanArrayArg(argName: String): ReadOnlyProperty<SupportFragment, BooleanArray?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getBooleanArray(argName) }


// Byte
fun SupportFragment.bindByteArg(argName: String, defaultValue: Byte = 0): ReadOnlyProperty<SupportFragment, Byte> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getByte(argName, defaultValue) }

fun SupportFragment.bindOptionalByteArg(argName: String, defaultValue: Byte = 0): ReadOnlyProperty<SupportFragment, Byte?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getByte(argName, defaultValue) }

// ByteArray
fun SupportFragment.bindByteArrayArg(argName: String): ReadOnlyProperty<SupportFragment, ByteArray> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getByteArray(argName) }

fun SupportFragment.bindOptionalByteArrayArg(argName: String): ReadOnlyProperty<SupportFragment, ByteArray?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getByteArray(argName) }


// Char
fun SupportFragment.bindCharArg(argName: String, defaultValue: Char = 0.toChar()): ReadOnlyProperty<SupportFragment, Char> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getChar(argName, defaultValue) }

fun SupportFragment.bindOptionalCharArg(argName: String, defaultValue: Char = 0.toChar()): ReadOnlyProperty<SupportFragment, Char?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getChar(argName, defaultValue) }

// CharArray
fun SupportFragment.bindCharArrayArg(argName: String): ReadOnlyProperty<SupportFragment, CharArray> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getCharArray(argName) }

fun SupportFragment.bindOptionalCharArrayArg(argName: String): ReadOnlyProperty<SupportFragment, CharArray?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getCharArray(argName) }


// Short
fun SupportFragment.bindShortArg(argName: String, defaultValue: Short = 0): ReadOnlyProperty<SupportFragment, Short> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getShort(argName, defaultValue) }

fun SupportFragment.bindOptionalShortArg(argName: String, defaultValue: Short = 0): ReadOnlyProperty<SupportFragment, Short?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getShort(argName, defaultValue) }

// ShortArray
fun SupportFragment.bindShortArrayArg(argName: String): ReadOnlyProperty<SupportFragment, ShortArray> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getShortArray(argName) }

fun SupportFragment.bindOptionalShortArrayArg(argName: String): ReadOnlyProperty<SupportFragment, ShortArray?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getShortArray(argName) }


// Float
fun SupportFragment.bindFloatArg(argName: String, defaultValue: Float = 0f): ReadOnlyProperty<SupportFragment, Float> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getFloat(argName, defaultValue) }

fun SupportFragment.bindOptionalFloatArg(argName: String, defaultValue: Float = 0f): ReadOnlyProperty<SupportFragment, Float?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getFloat(argName, defaultValue) }

// FloatArray
fun SupportFragment.bindFloatArrayArg(argName: String): ReadOnlyProperty<SupportFragment, FloatArray> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getFloatArray(argName) }

fun SupportFragment.bindOptionalFloatArrayArg(argName: String): ReadOnlyProperty<SupportFragment, FloatArray?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getFloatArray(argName) }


// Int
fun SupportFragment.bindIntArg(argName: String, defaultValue: Int = 0): ReadOnlyProperty<SupportFragment, Int> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getInt(argName, defaultValue) }

fun SupportFragment.bindOptionalIntArg(argName: String, defaultValue: Int = 0): ReadOnlyProperty<SupportFragment, Int?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getInt(argName, defaultValue) }

// IntArray
fun SupportFragment.bindIntArrayArg(argName: String): ReadOnlyProperty<SupportFragment, IntArray> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getIntArray(argName) }

fun SupportFragment.bindOptionalIntArrayArg(argName: String): ReadOnlyProperty<SupportFragment, IntArray?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getIntArray(argName) }

// ArrayList<Int>
fun SupportFragment.bindIntArrayListArg(argName: String): ReadOnlyProperty<SupportFragment, ArrayList<Int>> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getIntegerArrayList(argName) }

fun SupportFragment.bindOptionalIntArrayListArg(argName: String): ReadOnlyProperty<SupportFragment, ArrayList<Int>?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getIntegerArrayList(argName) }


// Double
fun SupportFragment.bindDoubleArg(argName: String, defaultValue: Double = 0.toDouble()): ReadOnlyProperty<SupportFragment, Double> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getDouble(argName, defaultValue) }

fun SupportFragment.bindOptionalDoubleArg(argName: String, defaultValue: Double = 0.toDouble()): ReadOnlyProperty<SupportFragment, Double?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getDouble(argName, defaultValue) }

// DoubleArray
fun SupportFragment.bindDoubleArrayArg(argName: String): ReadOnlyProperty<SupportFragment, DoubleArray> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getDoubleArray(argName) }

fun SupportFragment.bindOptionalDoubleArrayArg(argName: String): ReadOnlyProperty<SupportFragment, DoubleArray?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getDoubleArray(argName) }


// Long
fun SupportFragment.bindLongArg(argName: String, defaultValue: Long = 0L): ReadOnlyProperty<SupportFragment, Long> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getLong(argName, defaultValue) }

fun SupportFragment.bindOptionalLongArg(argName: String, defaultValue: Long = 0L): ReadOnlyProperty<SupportFragment, Long?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getLong(argName, defaultValue) }

// LongArray
fun SupportFragment.bindLongArrayArg(argName: String): ReadOnlyProperty<SupportFragment, LongArray> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getLongArray(argName) }

fun SupportFragment.bindOptionalLongArrayArg(argName: String): ReadOnlyProperty<SupportFragment, LongArray?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getLongArray(argName) }


// CharSequence
fun SupportFragment.bindCharSequenceArg(argName: String, defaultValue: CharSequence? = null): ReadOnlyProperty<SupportFragment, CharSequence> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getCharSequence(argName, defaultValue) }

fun SupportFragment.bindOptionalCharSequenceArg(argName: String, defaultValue: CharSequence? = null): ReadOnlyProperty<SupportFragment, CharSequence?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getCharSequence(argName, defaultValue) }

// Array<CharSequence>
fun SupportFragment.bindCharSequenceArrayArg(argName: String): ReadOnlyProperty<SupportFragment, Array<CharSequence>> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getCharSequenceArray(argName) }

fun SupportFragment.bindOptionalCharSequenceArrayArg(argName: String): ReadOnlyProperty<SupportFragment, Array<CharSequence>?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getCharSequenceArray(argName) }


// String
fun SupportFragment.bindStringArg(argName: String): ReadOnlyProperty<SupportFragment, String> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getString(argName) }

fun SupportFragment.bindOptionalStringArg(argName: String): ReadOnlyProperty<SupportFragment, String?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getString(argName) }

// Array<String>
fun SupportFragment.bindStringArrayArg(argName: String): ReadOnlyProperty<SupportFragment, Array<String>> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getStringArray(argName) }

fun SupportFragment.bindOptionalStringArrayArg(argName: String): ReadOnlyProperty<SupportFragment, Array<String>?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getStringArray(argName) }

// ArrayList<String>
fun SupportFragment.bindStringArrayListArg(argName: String): ReadOnlyProperty<SupportFragment, ArrayList<String>> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getStringArrayList(argName) }

fun SupportFragment.bindOptionalStringArrayListArg(argName: String): ReadOnlyProperty<SupportFragment, ArrayList<String>?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getStringArrayList(argName) }


// Parcelable
fun <V : Parcelable> SupportFragment.bindParcelableArg(argName: String): ReadOnlyProperty<SupportFragment, V> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getParcelable(argName) }

fun <V : Parcelable> SupportFragment.bindOptionalParcelableArg(argName: String): ReadOnlyProperty<SupportFragment, V?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getParcelable(argName) }

// Array<Parcelable>
@Suppress("UNCHECKED_CAST")
fun <V : Parcelable> SupportFragment.bindParcelableArrayArg(argName: String): ReadOnlyProperty<SupportFragment, Array<V>> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getParcelableArray(argName) as Array<V> }

@Suppress("UNCHECKED_CAST")
fun <V : Parcelable> SupportFragment.bindOptionalParcelableArrayArg(argName: String): ReadOnlyProperty<SupportFragment, Array<V>?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getParcelableArray(argName) as Array<V>? }

// ArrayList<Parcelable>
fun <V : Parcelable> SupportFragment.bindParcelableArrayListArg(argName: String): ReadOnlyProperty<SupportFragment, ArrayList<V>> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getParcelableArrayList(argName) }

fun <V : Parcelable> SupportFragment.bindOptionalParcelableArrayListArg(argName: String): ReadOnlyProperty<SupportFragment, ArrayList<V>?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getParcelableArrayList(argName) }

// SparseArray<Parcelable>
fun <V : Parcelable> SupportFragment.bindSparseParcelableArrayArg(argName: String): ReadOnlyProperty<SupportFragment, SparseArray<V>> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getSparseParcelableArray(argName) }

fun <V : Parcelable> SupportFragment.bindOptionalSparseParcelableArrayArg(argName: String): ReadOnlyProperty<SupportFragment, SparseArray<V>?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getSparseParcelableArray(argName) }

// Serializable
@Suppress("UNCHECKED_CAST")
fun <V : java.io.Serializable> SupportFragment.bindSerializableArg(argName: String): ReadOnlyProperty<SupportFragment, V> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getSerializable(argName) as V }

@Suppress("UNCHECKED_CAST")
fun <V : java.io.Serializable> SupportFragment.bindOptionalSerializableArg(argName: String): ReadOnlyProperty<SupportFragment, V?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getSerializable(argName) as V? }


// IBinder
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
fun SupportFragment.bindBinderArg(argName: String): ReadOnlyProperty<SupportFragment, IBinder> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getBinder(argName) }

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
fun SupportFragment.bindOptionalBinderArg(argName: String): ReadOnlyProperty<SupportFragment, IBinder?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getBinder(argName) }


// Bundle
fun SupportFragment.bindBundleArg(argName: String): ReadOnlyProperty<SupportFragment, Bundle> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getBundle(argName) }

fun SupportFragment.bindOptionalBundleArg(argName: String): ReadOnlyProperty<SupportFragment, Bundle?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getBundle(argName) }


// Size
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun SupportFragment.bindSizeArg(argName: String): ReadOnlyProperty<SupportFragment, Size> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getSize(argName) }

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun SupportFragment.bindOptionalSizeArg(argName: String): ReadOnlyProperty<SupportFragment, Size?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getSize(argName) }


// SizeF
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun SupportFragment.bindSizeFArg(argName: String): ReadOnlyProperty<SupportFragment, SizeF> =
        ArgLazy(argName) { ref, _: KProperty<*> -> requireNotNull(ref.arguments, { "arguments is null" }).getSizeF(argName) }

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun SupportFragment.bindOptionalSizeFArg(argName: String): ReadOnlyProperty<SupportFragment, SizeF?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.arguments?.getSizeF(argName) }


/* ************************************* Activity ***************************************** */

// Boolean
fun Activity.bindBooleanArg(argName: String, defaultValue: Boolean = false): ReadOnlyProperty<Activity, Boolean> =
        ArgLazy(argName) { ref, _: KProperty<*> -> ref.intent.getBooleanExtra(argName, defaultValue) }

fun Activity.bindOptionalBooleanArg(argName: String, defaultValue: Boolean = false): ReadOnlyProperty<Activity, Boolean?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.intent.getBooleanExtra(argName, defaultValue) }

// BooleanArray
fun Activity.bindBooleanArrayArg(argName: String): ReadOnlyProperty<Activity, BooleanArray> =
        ArgLazy(argName) { ref, _: KProperty<*> -> ref.intent.getBooleanArrayExtra(argName) }

fun Activity.bindOptionalBooleanArrayArg(argName: String): ReadOnlyProperty<Activity, BooleanArray?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.intent.getBooleanArrayExtra(argName) }


// Byte
fun Activity.bindByteArg(argName: String, defaultValue: Byte = 0): ReadOnlyProperty<Activity, Byte> =
        ArgLazy(argName) { ref, _: KProperty<*> -> ref.intent.getByteExtra(argName, defaultValue) }

fun Activity.bindOptionalByteArg(argName: String, defaultValue: Byte = 0): ReadOnlyProperty<Activity, Byte?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.intent.getByteExtra(argName, defaultValue) }

// ByteArray
fun Activity.bindByteArrayArg(argName: String): ReadOnlyProperty<Activity, ByteArray> =
        ArgLazy(argName) { ref, _: KProperty<*> -> ref.intent.getByteArrayExtra(argName) }

fun Activity.bindOptionalByteArrayArg(argName: String): ReadOnlyProperty<Activity, ByteArray?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.intent.getByteArrayExtra(argName) }


// Char
fun Activity.bindCharArg(argName: String, defaultValue: Char = 0.toChar()): ReadOnlyProperty<Activity, Char> =
        ArgLazy(argName) { ref, _: KProperty<*> -> ref.intent.getCharExtra(argName, defaultValue) }

fun Activity.bindOptionalCharArg(argName: String, defaultValue: Char = 0.toChar()): ReadOnlyProperty<Activity, Char?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.intent.getCharExtra(argName, defaultValue) }

// CharArray
fun Activity.bindCharArrayArg(argName: String): ReadOnlyProperty<Activity, CharArray> =
        ArgLazy(argName) { ref, _: KProperty<*> -> ref.intent.getCharArrayExtra(argName) }

fun Activity.bindOptionalCharArrayArg(argName: String): ReadOnlyProperty<Activity, CharArray?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.intent.getCharArrayExtra(argName) }


// Short
fun Activity.bindShortArg(argName: String, defaultValue: Short = 0): ReadOnlyProperty<Activity, Short> =
        ArgLazy(argName) { ref, _: KProperty<*> -> ref.intent.getShortExtra(argName, defaultValue) }

fun Activity.bindOptionalShortArg(argName: String, defaultValue: Short = 0): ReadOnlyProperty<Activity, Short?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.intent.getShortExtra(argName, defaultValue) }

// ShortArray
fun Activity.bindShortArrayArg(argName: String): ReadOnlyProperty<Activity, ShortArray> =
        ArgLazy(argName) { ref, _: KProperty<*> -> ref.intent.getShortArrayExtra(argName) }

fun Activity.bindOptionalShortArrayArg(argName: String): ReadOnlyProperty<Activity, ShortArray?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.intent.getShortArrayExtra(argName) }


// Float
fun Activity.bindFloatArg(argName: String, defaultValue: Float = 0f): ReadOnlyProperty<Activity, Float> =
        ArgLazy(argName) { ref, _: KProperty<*> -> ref.intent.getFloatExtra(argName, defaultValue) }

fun Activity.bindOptionalFloatArg(argName: String, defaultValue: Float = 0f): ReadOnlyProperty<Activity, Float?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.intent.getFloatExtra(argName, defaultValue) }

// FloatArray
fun Activity.bindFloatArrayArg(argName: String): ReadOnlyProperty<Activity, FloatArray> =
        ArgLazy(argName) { ref, _: KProperty<*> -> ref.intent.getFloatArrayExtra(argName) }

fun Activity.bindOptionalFloatArrayArg(argName: String): ReadOnlyProperty<Activity, FloatArray?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.intent.getFloatArrayExtra(argName) }


// Int
fun Activity.bindIntArg(argName: String, defaultValue: Int = 0): ReadOnlyProperty<Activity, Int> =
        ArgLazy(argName) { ref, _: KProperty<*> -> ref.intent.getIntExtra(argName, defaultValue) }

fun Activity.bindOptionalIntArg(argName: String, defaultValue: Int = 0): ReadOnlyProperty<Activity, Int?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.intent.getIntExtra(argName, defaultValue) }

// IntArray
fun Activity.bindIntArrayArg(argName: String): ReadOnlyProperty<Activity, IntArray> =
        ArgLazy(argName) { ref, _: KProperty<*> -> ref.intent.getIntArrayExtra(argName) }

fun Activity.bindOptionalIntArrayArg(argName: String): ReadOnlyProperty<Activity, IntArray?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.intent.getIntArrayExtra(argName) }

// ArrayList<Int>
fun Activity.bindIntArrayListArg(argName: String): ReadOnlyProperty<Activity, ArrayList<Int>> =
        ArgLazy(argName) { ref, _: KProperty<*> -> ref.intent.getIntegerArrayListExtra(argName) }

fun Activity.bindOptionalIntArrayListArg(argName: String): ReadOnlyProperty<Activity, ArrayList<Int>?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.intent.getIntegerArrayListExtra(argName) }


// Double
fun Activity.bindDoubleArg(argName: String, defaultValue: Double = 0.toDouble()): ReadOnlyProperty<Activity, Double> =
        ArgLazy(argName) { ref, _: KProperty<*> -> ref.intent.getDoubleExtra(argName, defaultValue) }

fun Activity.bindOptionalDoubleArg(argName: String, defaultValue: Double = 0.toDouble()): ReadOnlyProperty<Activity, Double?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.intent.getDoubleExtra(argName, defaultValue) }

// DoubleArray
fun Activity.bindDoubleArrayArg(argName: String): ReadOnlyProperty<Activity, DoubleArray> =
        ArgLazy(argName) { ref, _: KProperty<*> -> ref.intent.getDoubleArrayExtra(argName) }

fun Activity.bindOptionalDoubleArrayArg(argName: String): ReadOnlyProperty<Activity, DoubleArray?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.intent.getDoubleArrayExtra(argName) }


// Long
fun Activity.bindLongArg(argName: String, defaultValue: Long = 0L): ReadOnlyProperty<Activity, Long> =
        ArgLazy(argName) { ref, _: KProperty<*> -> ref.intent.getLongExtra(argName, defaultValue) }

fun Activity.bindOptionalLongArg(argName: String, defaultValue: Long = 0L): ReadOnlyProperty<Activity, Long?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.intent.getLongExtra(argName, defaultValue) }

// LongArray
fun Activity.bindLongArrayArg(argName: String): ReadOnlyProperty<Activity, LongArray> =
        ArgLazy(argName) { ref, _: KProperty<*> -> ref.intent.getLongArrayExtra(argName) }

fun Activity.bindOptionalLongArrayArg(argName: String): ReadOnlyProperty<Activity, LongArray?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.intent.getLongArrayExtra(argName) }


// CharSequence
fun Activity.bindCharSequenceArg(argName: String): ReadOnlyProperty<Activity, CharSequence> =
        ArgLazy(argName) { ref, _: KProperty<*> -> ref.intent.getCharSequenceExtra(argName) }

fun Activity.bindOptionalCharSequenceArg(argName: String): ReadOnlyProperty<Activity, CharSequence?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.intent.getCharSequenceExtra(argName) }

// Array<CharSequence>
fun Activity.bindCharSequenceArrayArg(argName: String): ReadOnlyProperty<Activity, Array<CharSequence>> =
        ArgLazy(argName) { ref, _: KProperty<*> -> ref.intent.getCharSequenceArrayExtra(argName) }

fun Activity.bindOptionalCharSequenceArrayArg(argName: String): ReadOnlyProperty<Activity, Array<CharSequence>?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.intent.getCharSequenceArrayExtra(argName) }


// String
fun Activity.bindStringArg(argName: String): ReadOnlyProperty<Activity, String> =
        ArgLazy(argName) { ref, _: KProperty<*> -> ref.intent.getStringExtra(argName) }

fun Activity.bindOptionalStringArg(argName: String): ReadOnlyProperty<Activity, String?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.intent.getStringExtra(argName) }

// Array<String>
fun Activity.bindStringArrayArg(argName: String): ReadOnlyProperty<Activity, Array<String>> =
        ArgLazy(argName) { ref, _: KProperty<*> -> ref.intent.getStringArrayExtra(argName) }

fun Activity.bindOptionalStringArrayArg(argName: String): ReadOnlyProperty<Activity, Array<String>?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.intent.getStringArrayExtra(argName) }

// ArrayList<String>
fun Activity.bindStringArrayListArg(argName: String): ReadOnlyProperty<Activity, ArrayList<String>> =
        ArgLazy(argName) { ref, _: KProperty<*> -> ref.intent.getStringArrayListExtra(argName) }

fun Activity.bindOptionalStringArrayListArg(argName: String): ReadOnlyProperty<Activity, ArrayList<String>?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.intent.getStringArrayListExtra(argName) }


// Parcelable
fun <V : Parcelable> Activity.bindParcelableArg(argName: String): ReadOnlyProperty<Activity, V> =
        ArgLazy(argName) { ref, _: KProperty<*> -> ref.intent.getParcelableExtra(argName) }

fun <V : Parcelable> Activity.bindOptionalParcelableArg(argName: String): ReadOnlyProperty<Activity, V?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.intent.getParcelableExtra(argName) }

// Array<Parcelable>
@Suppress("UNCHECKED_CAST")
fun <V : Parcelable> Activity.bindParcelableArrayArg(argName: String): ReadOnlyProperty<Activity, Array<V>> =
        ArgLazy(argName) { ref, _: KProperty<*> -> ref.intent.getParcelableArrayExtra(argName) as Array<V> }

@Suppress("UNCHECKED_CAST")
fun <V : Parcelable> Activity.bindOptionalParcelableArrayArg(argName: String): ReadOnlyProperty<Activity, Array<V>?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.intent.getParcelableArrayExtra(argName) as Array<V>? }

// ArrayList<Parcelable>
fun <V : Parcelable> Activity.bindParcelableArrayListArg(argName: String): ReadOnlyProperty<Activity, ArrayList<V>> =
        ArgLazy(argName) { ref, _: KProperty<*> -> ref.intent.getParcelableArrayListExtra(argName) }

fun <V : Parcelable> Activity.bindOptionalParcelableArrayListArg(argName: String): ReadOnlyProperty<Activity, ArrayList<V>?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.intent.getParcelableArrayListExtra(argName) }

// Serializable
@Suppress("UNCHECKED_CAST")
fun <V : java.io.Serializable> Activity.bindSerializableArg(argName: String): ReadOnlyProperty<Activity, V> =
        ArgLazy(argName) { ref, _: KProperty<*> -> ref.intent.getSerializableExtra(argName) as V }

@Suppress("UNCHECKED_CAST")
fun <V : java.io.Serializable> Activity.bindOptionalSerializableArg(argName: String): ReadOnlyProperty<Activity, V?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.intent.getSerializableExtra(argName) as V? }


// Bundle
fun Activity.bindBundleArg(argName: String): ReadOnlyProperty<Activity, Bundle> =
        ArgLazy(argName) { ref, _: KProperty<*> -> ref.intent.getBundleExtra(argName) }

fun Activity.bindOptionalBundleArg(argName: String): ReadOnlyProperty<Activity, Bundle?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.intent.getBundleExtra(argName) }

// Extras Bundle
fun Activity.bindExtrasArg(): ReadOnlyProperty<Activity, Bundle> =
        ArgLazy("extras") { ref, _: KProperty<*> -> ref.intent.extras }

fun Activity.bindOptionalExtrasArg(): ReadOnlyProperty<Activity, Bundle?> =
        OptionalArgLazy { ref, _: KProperty<*> -> ref.intent.extras }


private class ArgLazy<in REF, out OUT : Any>(val argName: String, val initializer: (REF, KProperty<*>) -> OUT?) : ReadOnlyProperty<REF, OUT> {
    private object EMPTY

    var arg: Any = EMPTY

    override fun getValue(thisRef: REF, property: KProperty<*>): OUT {
        if (arg == EMPTY) {
            arg = requireNotNull(initializer(thisRef, property), { "Not found arg '$argName' from arguments. 2" })
        }
        @Suppress("UNCHECKED_CAST")
        return arg as OUT
    }
}

private class OptionalArgLazy<in REF, out OUT>(val initializer: (REF, KProperty<*>) -> OUT?) : ReadOnlyProperty<REF, OUT?> {
    private object EMPTY

    var arg: Any? = EMPTY

    override fun getValue(thisRef: REF, property: KProperty<*>): OUT? {
        if (arg == EMPTY) {
            arg = initializer(thisRef, property)
        }
        @Suppress("UNCHECKED_CAST")
        return arg as OUT
    }
}