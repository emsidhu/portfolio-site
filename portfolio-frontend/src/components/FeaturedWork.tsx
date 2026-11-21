import FeaturedProject from "./FeaturedProject";
import SectionHeader from "./SectionHeader";

function FeaturedWork() {
  return (
    <div className="p-4 md:p-20 bg-gray-900 text-gray-50">
      <SectionHeader text="Featured Work" />
      <div className="grid grid-cols-12 mt-8">
        <FeaturedProject />
        <FeaturedProject />
        <FeaturedProject />
      </div>
    </div>
  );
}
export default FeaturedWork;
