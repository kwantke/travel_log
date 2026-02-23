import { FORM_VALIDATIONS_MAP } from "@constants/formValidation";
import useGetTags from "@queries/useGetTags";
import { useCallback, useState } from "react";

const useMultiSelectionTag = (key?: string) => {
  const { data: tags } = useGetTags();

  const [selectedTagIDs, setSelectedTagIDs] = useState<number[]>(
    key ? JSON.parse(localStorage.getItem(key) ?? "[]") : [],
  );

  const createSortedTags = () => {
    if (!tags) return [];

    const selected = tags.filter((tag) => selectedTagIDs.includes(tag.id));
    const unselected = tags.filter((tag) => !selectedTagIDs.includes(tag.id));
    return [...selected, ...unselected];
  };

  // const handleChangeSelectedTagIDs = useCallback(
  //   (newSelectedTagIDs: number[]) => {
  //     setSelectedTagIDs(newSelectedTagIDs);
  //   },
  //   [],
  // );

  const handleClickTag = (id: number) => {
    setSelectedTagIDs((preSelectedTagIDs) => {
      const newSelectedTagIDs = preSelectedTagIDs.includes(id)
        ? preSelectedTagIDs.filter((selectedTagID) => selectedTagID !== id)
        : [...preSelectedTagIDs, id];

      const isTagIDsSelectedMax =
        newSelectedTagIDs.length > FORM_VALIDATIONS_MAP.tags.maxCount;

      if (isTagIDsSelectedMax && key)
        localStorage.setItem(key, JSON.stringify(preSelectedTagIDs));
      if (isTagIDsSelectedMax) return preSelectedTagIDs;

      increaseMultiSelectionTagAnimationKey();
      if (key) {
        localStorage.setItem(key, JSON.stringify(newSelectedTagIDs));
        window.scrollTo({ top: 0 });
      }

      return newSelectedTagIDs;
    });
  };

  const [multiSelectionTagAnimationKey, setMultiSelectionTagAnimationKey] =
    useState(0);

  const increaseMultiSelectionTagAnimationKey = () => {
    setMultiSelectionTagAnimationKey((prev) => prev + 1);
  };

  const resetMultiSelectionTag = () => {
    setSelectedTagIDs([]);
    if (key) localStorage.setItem(key, JSON.stringify([]));
    increaseMultiSelectionTagAnimationKey();
  };

  return {
    selectedTagIDs,
    sortedTags: createSortedTags(),
    handleClickTag,
    multiSelectionTagAnimationKey,
    resetMultiSelectionTag,
  };
};

export default useMultiSelectionTag;
