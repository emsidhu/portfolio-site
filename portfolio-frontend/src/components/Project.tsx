import TechPill from "./TechPill";

function Project() {
  return (
    <div className="w-full border rounded-3xl overflow-hidden">
      <div className="border-b">
        <img
          src="https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg"
          alt="Project Image"
          className="w-full max-h-[40lvw] md:max-h-[30lvw] xl:max-h-[20lvw] object-cover"
        />
      </div>
      <div className="p-4 bg-linear-to-br from-blue-300 to-blue-400">
        <div className="grid grid-cols-2">
          <div className="flex gap-2 items-center justify-self-start">
            <h3 className="inline m-0 text-2xl font-bold">Title</h3>
            <TechPill text="techName" />
          </div>

          <div className="flex justify-self-end text-3xl gap-2">
            <a href="">
              <i className="bi bi-play-circle" />
            </a>
            <a href="https://github.com/emsidhu" target="_blank" rel="noreferrer">
              <i className="bi bi-github" />
            </a>
          </div>
        </div>
        <p className="list-item list-disc ml-5 pt-1 text-md">Last Updated: [date]</p>
        <p className="pt-1 text-lg">
          Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt
          ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation
          ullamco laboris nisi ut aliquip ex ea commodo consequat.
        </p>
      </div>
    </div>
  );
}
export default Project;
