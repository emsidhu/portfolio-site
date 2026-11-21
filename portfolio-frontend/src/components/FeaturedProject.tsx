import TechPill from "./TechPill";

function FeaturedProject() {
  return (
    <div className="col-span-12 lg:col-span-4 m-4 rounded-3xl overflow-hidden text-black">
      <div className="relative border-b">
        <img
          src="https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg"
          alt="Project Image"
          className="w-full max-h-[40lvw] md:max-h-[30lvw] lg:max-h-full object-cover"
        />
        <span className="absolute top-2 right-2">
          <TechPill text={"technology"} />
        </span>
      </div>
      <div className="grid grid-rows-2 p-2 lg:px-1 lg:pb-0 text-gray-900 bg-linear-to-br from-purple-300 to-purple-400">
        <div className="flex items-center text-3xl font-extrabold px-2">
          <a href="">
            <i className="bi bi-play-circle" />
          </a>
          <h3 className="grow text-center">Randoop Recreation</h3>

          <a target="_blank" rel="noreferrer">
            <i className="bi bi-github" />
          </a>
        </div>

        <p className="text-center mt-2 pb-1 lg:pb-2 text-xl font-bold">Description</p>
      </div>
    </div>
  );
}
export default FeaturedProject;
