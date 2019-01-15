package com.codebosses.flicks.pojo.expandrecyclerviewpojo;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class CategoryHeader extends ExpandableGroup<CategoryItem> {

  private int iconResId;

  public CategoryHeader(String title, List<CategoryItem> items, int iconResId) {
    super(title, items);
    this.iconResId = iconResId;
  }

  public int getIconResId() {
    return iconResId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof CategoryHeader)) return false;

    CategoryHeader genre = (CategoryHeader) o;

    return getIconResId() == genre.getIconResId();

  }

  @Override
  public int hashCode() {
    return getIconResId();
  }
}

