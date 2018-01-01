/*
 * Copyright (C) 2017 Jared Rummler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jaredrummler.android.colorpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.AttributeSet;

/**
 * A Preference to select a color
 */
public class ColorPreference extends DialogPreference {

  private static final int SIZE_NORMAL = 0;
  private static final int SIZE_LARGE = 1;

  private int color = Color.BLACK;
  @ColorPickerDialog.DialogType
  private int dialogType;
  private int colorShape;
  private boolean allowPresets;
  private boolean allowCustom;
  private boolean showAlphaSlider;
  private boolean showColorShades;
  private int previewSize;
  private int[] presets;
  private int dialogTitle;

  public ColorPreference(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  public ColorPreference(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(attrs);
  }

  private void init(AttributeSet attrs) {
    setPersistent(true);
    TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ColorPreference);
    //noinspection WrongConstant
    dialogType = a.getInt(R.styleable.ColorPreference_cpv_dialogType, ColorPickerDialog.TYPE_PRESETS);
    colorShape = a.getInt(R.styleable.ColorPreference_cpv_colorShape, ColorShape.CIRCLE);
    allowPresets = a.getBoolean(R.styleable.ColorPreference_cpv_allowPresets, true);
    allowCustom = a.getBoolean(R.styleable.ColorPreference_cpv_allowCustom, true);
    showAlphaSlider = a.getBoolean(R.styleable.ColorPreference_cpv_showAlphaSlider, false);
    showColorShades = a.getBoolean(R.styleable.ColorPreference_cpv_showColorShades, true);
    previewSize = a.getInt(R.styleable.ColorPreference_cpv_previewSize, SIZE_NORMAL);
    final int presetsResId = a.getResourceId(R.styleable.ColorPreference_cpv_colorPresets, 0);
    dialogTitle = a.getResourceId(R.styleable.ColorPreference_cpv_dialogTitle, R.string.cpv_default_title);
    if (presetsResId != 0) {
      presets = getContext().getResources().getIntArray(presetsResId);
    } else {
      presets = ColorPickerDialog.MATERIAL_COLORS;
    }
    if (colorShape == ColorShape.CIRCLE) {
      setWidgetLayoutResource(
          previewSize == SIZE_LARGE ? R.layout.cpv_preference_circle_large : R.layout.cpv_preference_circle);
    } else {
      setWidgetLayoutResource(
          previewSize == SIZE_LARGE ? R.layout.cpv_preference_square_large : R.layout.cpv_preference_square
      );
    }
    a.recycle();
  }

  @Override
  public void onBindViewHolder(PreferenceViewHolder holder) {
    super.onBindViewHolder(holder);

    ColorPanelView preview = (ColorPanelView) holder.findViewById(R.id.cpv_preference_preview_color_panel);
    if (preview != null) {
      preview.setColor(color);
    }
  }

  @Override protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
    if (restorePersistedValue) {
      color = getPersistedInt(0xFF000000);
    } else {
      color = (Integer) defaultValue;
      persistInt(color);
    }
  }

  @Override protected Object onGetDefaultValue(TypedArray a, int index) {
    return a.getInteger(index, Color.BLACK);
  }

  /**
   * Set the new color
   *
   * @param color
   *     The newly selected color
   */
  public void saveValue(@ColorInt int color) {
    this.color = color;
    persistInt(this.color);
    notifyChanged();
    callChangeListener(color);
  }

  /**
   * Set the colors shown in the {@link ColorPickerDialog}.
   *
   * @param presets An array of color ints
   */
  public void setPresets(@NonNull int[] presets) {
    this.presets = presets;
  }

  /**
   * Get the colors that will be shown in the {@link ColorPickerDialog}.
   *
   * @return An array of color ints
   */
  public int[] getPresets() {
    return presets;
  }

  public ColorPickerDialog createDialog() {
    ColorPickerDialog dialog = ColorPickerDialog.newBuilder()
      .setPreferenceKey(getKey())
      .setDialogType(dialogType)
      .setDialogTitle(dialogTitle)
      .setColorShape(colorShape)
      .setPresets(presets)
      .setAllowPresets(allowPresets)
      .setAllowCustom(allowCustom)
      .setShowAlphaSlider(showAlphaSlider)
      .setShowColorShades(showColorShades)
      .setColor(color)
      .create();
    return dialog;
  }
}
