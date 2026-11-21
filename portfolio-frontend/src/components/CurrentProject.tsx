import SectionHeader from "./SectionHeader";
import TechPill from "./TechPill";

function CurrentProject() {
  return (
    <div className="max-w-full sm:max-w-[90vw] md:max-w-[85vw] mx-auto mt-4">
      <SectionHeader text="Current Project" />

      <div className="w-full grid grid-cols-1 md:grid-cols-2 border rounded-3xl overflow-hidden">
        <div className="border-b md:border-r">
          <img
            src="https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg"
            alt="Project Image"
            className="w-full h-full object-cover"
          />
        </div>
        <div className="p-4 bg-linear-to-br from-blue-300 to-blue-400">
          <div className="grid grid-cols-2">
            <div className="flex gap-2 items-center justify-self-start">
              <h3 className="inline m-0 text-2xl font-bold">Title</h3>
              <TechPill text="techName" />
            </div>

            <div className="flex justify-self-end text-2xl gap-2">
              <a href="">
                <i className="bi bi-play-circle" />
              </a>
              <a href="https://github.com/emsidhu" target="_blank" rel="noreferrer">
                <i className="bi bi-github" />
              </a>
            </div>
          </div>
          <p className="list-item list-disc ml-5 pt-1 text-md">Last Updated: [date]</p>
          <p className="pt-2 text-lg">
            Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor
            incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud
            exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.
          </p>
        </div>
      </div>
    </div>
  );
}
export default CurrentProject;
