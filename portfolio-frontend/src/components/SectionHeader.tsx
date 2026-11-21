function SectionHeader({ text }: { text: string }) {
  return (
    <div className="underline decoration-orange-500 text-center text-5xl w-full lg:text-6xl py-10 md:pt-0 font-extrabold">
      {text}
    </div>
  );
}

export default SectionHeader;
