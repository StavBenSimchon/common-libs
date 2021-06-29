def test(){
  def c = libraryResource 'globalconfig.yml'
  def c_map = readYaml text:c
  return c_map
}
def ctest(String fp){
  def c = libraryResource 'globalconfig.yml'
  def c_map = readYaml text:c

  if(!fp.isEmpty()){
      def file_exist = findFiles (glob: fp)
      if(file_exist.length == 1){
          def t_map = readYaml file:fp
          c_map.putAll(t_map)
      }
  }


}