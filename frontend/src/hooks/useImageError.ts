import { useState } from "react";

const useImageError = ({ imageUrl }: { imageUrl?: string }) => {
  const [loadFailed, setLoadFailed] = useState(false);

  const handleImageError = () => setLoadFailed(true);

  return { imageError: !imageUrl || loadFailed, handleImageError };
};
export default useImageError;
